package com.wavemaker.connector.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.wavemaker.connector.properties.S3PropertiesService;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 14/2/20
 */
public abstract class AbstractS3Connector implements S3Connector {

    private AmazonS3 s3Client;

    private S3PropertiesService s3PropertiesService;

    public AbstractS3Connector(S3PropertiesService s3PropertiesService) {
        this.s3PropertiesService = s3PropertiesService;
    }

    public AmazonS3 getS3Client() throws SdkClientException {
        if (s3Client == null) {
            try {
                BasicAWSCredentials awsCreds = new BasicAWSCredentials(s3PropertiesService.getAccessKey(), s3PropertiesService.getAccessSecret());
                s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                        .withRegion(s3PropertiesService.getClientRegion())
                        .build();
            } catch (RuntimeException e) {
                throw new RuntimeException("Failed to build aws s3 client", e);
            }
        }
        return s3Client;
    }

    public S3PropertiesService getConfiguration() {
        return s3PropertiesService;
    }


}
