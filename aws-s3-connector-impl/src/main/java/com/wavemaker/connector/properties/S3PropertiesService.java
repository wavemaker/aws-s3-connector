package com.wavemaker.connector.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
/**
 * Created by saraswathir on 04/10/20
 */
@Service
public class S3PropertiesService {
    @Value("${aws.clientRegion}")
    private Regions clientRegion;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.accessSecret}")
    private String accessSecret;

    public Regions getClientRegion() {
        return clientRegion;
    }

    public S3PropertiesService setClientRegion(String clientRegion) {
        this.clientRegion = Regions.valueOf(clientRegion);
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public S3PropertiesService setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public S3PropertiesService setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public S3PropertiesService setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
        return this;
    }
}
