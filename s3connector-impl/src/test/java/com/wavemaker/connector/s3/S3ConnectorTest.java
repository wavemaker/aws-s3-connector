package com.wavemaker.connector.s3;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import com.wavemaker.connector.s3.S3Connector;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = S3ConnectorTestConfiguration.class)
public class S3ConnectorTest {

    @Autowired
    private S3Connector s3Connector;

    @Test
    public void listS3Objects() {
        List<WMS3ObjectSummary> wms3ObjectSummaries = s3Connector.listS3Objects();
    }

    @Test
    public void uploadFileToS3(){
        s3Connector.uploadFileToS3(new File("/home/sunilp/Downloads/IMG_20200202_115106.jpg"),null);
    }

    @Test
    public void downloadFile() throws IOException{
        s3Connector.downloadFile("IMG_20200202_115106.jpg");
    }

    @Test
    public void deleteFile(){
        s3Connector.deleteFile("IMG_20200202_115106.jpg");
    }
}