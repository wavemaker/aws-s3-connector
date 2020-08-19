package com.wavemaker.connector.s3;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.wavemaker.runtime.connector.annotation.WMConnector;


@WMConnector(name = "s3",
        description = "This connector provides apis to interact with AWS S3 bucket")
public interface S3Connector {

    List<WMS3ObjectSummary> listS3Objects();

    void uploadFileToS3(File file, Map<String, String> metadata);

    OutputStream downloadFile(String s3KeyName) throws IOException;

    void deleteFile(String s3KeyName);

}