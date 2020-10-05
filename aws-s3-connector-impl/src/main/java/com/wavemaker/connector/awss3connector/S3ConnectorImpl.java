package com.wavemaker.connector.awss3connector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.wavemaker.connector.properties.S3PropertiesService;

@Service
@Primary
public class S3ConnectorImpl extends AbstractS3Connector {

    private static final Logger logger = LoggerFactory.getLogger(S3ConnectorImpl.class);

    @Autowired
    public S3ConnectorImpl(S3PropertiesService s3PropertiesService) {
        super(s3PropertiesService);
    }

    /**
     * Returns a list of summary information about the objects in the specified bucket.
     *
     * @return: {@link AWSS3ObjectSummary object summary} A listing of the objects in the specified bucket, along with any other associated information
     */
    @Override
    public List<AWSS3ObjectSummary> listS3Objects() {
        logger.info("Listing S3 objects");
        List<AWSS3ObjectSummary> wms3ObjectSummaries = new ArrayList<>();
        ListObjectsRequest listRequest = new ListObjectsRequest().withBucketName(getConfiguration().getBucketName()).withMaxKeys(10);
        ObjectListing objects = getS3Client().listObjects(listRequest);
        while (true) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            for (S3ObjectSummary summary : summaries) {
                wms3ObjectSummaries.add(new AWSS3ObjectSummary()
                        .setETag(summary.getETag())
                        .setBucketName(summary.getBucketName())
                        .setKey(summary.getKey())
                        .setLastModified(summary.getLastModified())
                        .setSize(summary.getSize())
                        .setStorageClass(summary.getStorageClass()));
                System.out.println(summary);
            }
            if (objects.isTruncated()) {
                objects = getS3Client().listNextBatchOfObjects(objects);
            } else {
                break;
            }
        }
        return wms3ObjectSummaries;
    }

    /**
     * Uploads a new file object to the specified Amazon S3 bucket. You Must have WRITE permissions on bucket to add an object to it.
     *
     * @param file
     * @param metadata
     */
    @Override
    public void uploadFileToS3(File file, Map<String, String> metadata) {
        PutObjectRequest request = new PutObjectRequest(getConfiguration().getBucketName(), file.getName(), file);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            String mimeType = Files.probeContentType(file.toPath());
            objectMetadata.setContentType(mimeType);
        } catch (IOException e) {
            throw new RuntimeException("failed to find mime type of given file", e);
        }

        if (metadata != null) {
            for (Map.Entry entry : metadata.entrySet()) {
                objectMetadata.addUserMetadata(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        request.setMetadata(objectMetadata);
        getS3Client().putObject(request);
    }

    /**
     * Gets the object from Amazon S3 under the specified bucket and key. To get an object from Amazon S3, the caller must have READ permission to access the
     * object.
     *
     * @param s3KeyName
     * @return
     * @throws IOException
     */
    @Override
    public OutputStream downloadFile(String s3KeyName) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(getConfiguration().getBucketName(), s3KeyName);
        S3Object s3Object = getS3Client().getObject(getObjectRequest);
        File localFile = File.createTempFile(s3Object.getKey(), "");
        localFile.createNewFile();
        getS3Client().getObject(getObjectRequest, localFile);
        boolean success = localFile.exists() && localFile.canRead();
        return toOutPutStream(localFile);
    }

    private OutputStream toOutPutStream(File localFile) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis;
        byte[] buf = new byte[1024];
        try {
            fis = new FileInputStream(localFile);
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert file to output stream", ex);
        }
        return bos;
    }

    /**
     * Deletes the specified object in the specified bucket. Once deleted, the object can only be restored if versioning was enabled when the object was
     * deleted. If attempting to delete an object that does not exist, Amazon S3 will return a success message instead of an error message.
     *
     * @param s3KeyName
     */
    @Override
    public void deleteFile(String s3KeyName) {
        getS3Client().deleteObject(new DeleteObjectRequest(getConfiguration().getBucketName(), s3KeyName));
    }
}
