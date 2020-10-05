package com.wavemaker.connector.awss3connector;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.wavemaker.runtime.connector.annotation.WMConnector;


@WMConnector(name = "aws-s3-connector",
        description = "This connector provides apis to interact with AWS S3 bucket")
public interface S3Connector {

    /**
     * Returns a list of summary information about the objects in the specified bucket.
     *
     * @return: {@link AWSS3ObjectSummary object summary} A listing of the objects in the specified bucket, along with any other associated information
     */
    List<AWSS3ObjectSummary> listS3Objects();

    /**
     * Uploads a new file object to the specified Amazon S3 bucket. You Must have WRITE permissions on bucket to add an object to it.
     *
     * @param file
     * @param metadata
     */
    void uploadFileToS3(File file, Map<String, String> metadata);

    /**
     * Gets the object from Amazon S3 under the specified bucket and key. To get an object from Amazon S3, the caller must have READ permission to access the
     * object.
     *
     * @param s3KeyName
     * @return
     * @throws IOException
     */
    OutputStream downloadFile(String s3KeyName) throws IOException;

    /**
     * Deletes the specified object in the specified bucket. Once deleted, the object can only be restored if versioning was enabled when the object was
     * deleted. If attempting to delete an object that does not exist, Amazon S3 will return a success message instead of an error message.
     *
     * @param s3KeyName
     */
    void deleteFile(String s3KeyName);

}