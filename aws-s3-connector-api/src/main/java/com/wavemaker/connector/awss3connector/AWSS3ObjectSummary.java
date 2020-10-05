package com.wavemaker.connector.awss3connector;

import java.util.Date;

/**
 * Created by saraswathir on 04/10/20
 */
public class AWSS3ObjectSummary {
    protected String bucketName;
    protected String key;
    protected String eTag;
    protected long size;
    protected Date lastModified;
    protected String storageClass;

    public AWSS3ObjectSummary() {
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public AWSS3ObjectSummary setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public AWSS3ObjectSummary setKey(String key) {
        this.key = key;
        return this;
    }

    public String getETag() {
        return this.eTag;
    }

    public AWSS3ObjectSummary setETag(String eTag) {
        this.eTag = eTag;
        return this;
    }

    public long getSize() {
        return this.size;
    }

    public AWSS3ObjectSummary setSize(long size) {
        this.size = size;
        return this;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public AWSS3ObjectSummary setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getStorageClass() {
        return this.storageClass;
    }

    public AWSS3ObjectSummary setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public String toString() {
        return "S3ObjectSummary{bucketName='" + this.bucketName + '\'' + ", key='" + this.key + '\'' + ", eTag='" + this.eTag + '\'' + ", size=" + this.size + ", lastModified=" + this.lastModified + ", storageClass='" + this.storageClass + '}';
    }
}
