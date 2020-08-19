package com.wavemaker.connector.s3;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.*;
import com.wavemaker.connector.properties.S3PropertiesService;


@Service
@Primary
public class S3ConnectorImpl extends AbstractS3Connector{

    private static final Logger logger = LoggerFactory.getLogger(S3ConnectorImpl.class);

    @Autowired
    public S3ConnectorImpl(S3PropertiesService s3PropertiesService) {
        super(s3PropertiesService);
    }

    @Override
    public List<WMS3ObjectSummary> listS3Objects() {
        logger.info("Listing S3 objects");
        List<WMS3ObjectSummary> wms3ObjectSummaries = new ArrayList<>();
        ListObjectsRequest listRequest = new ListObjectsRequest().withBucketName(getConfiguration().getBucketName()).withMaxKeys(10);
        ObjectListing objects = getS3Client().listObjects(listRequest);
        while (true) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            for (S3ObjectSummary summary : summaries) {
                wms3ObjectSummaries.add(new WMS3ObjectSummary()
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

    @Override
    public OutputStream downloadFile(String s3KeyName) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(getConfiguration().getBucketName(), s3KeyName);
        S3Object s3Object = getS3Client().getObject(getObjectRequest);
        File localFile = File.createTempFile(s3Object.getKey(),"");
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
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert file to output stream",ex);
        }
        return bos;
    }

    @Override
    public void deleteFile(String s3KeyName) {
        getS3Client().deleteObject(new DeleteObjectRequest(getConfiguration().getBucketName(), s3KeyName));
    }
}