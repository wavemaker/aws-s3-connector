package com.wavemaker.connector.s3;


import java.util.Date;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 19/2/20
 */
public class WMS3ObjectSummary {

    protected String bucketName;
    protected String key;
    protected String eTag;
    protected long size;
    protected Date lastModified;
    protected String storageClass;

    public WMS3ObjectSummary() {
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public WMS3ObjectSummary setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public WMS3ObjectSummary setKey(String key) {
        this.key = key;
        return this;
    }

    public String getETag() {
        return this.eTag;
    }

    public WMS3ObjectSummary setETag(String eTag) {
        this.eTag = eTag;
        return this;
    }

    public long getSize() {
        return this.size;
    }

    public WMS3ObjectSummary setSize(long size) {
        this.size = size;
        return this;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public WMS3ObjectSummary setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getStorageClass() {
        return this.storageClass;
    }

    public WMS3ObjectSummary setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public String toString() {
        return "S3ObjectSummary{bucketName='" + this.bucketName + '\'' + ", key='" + this.key + '\'' + ", eTag='" + this.eTag + '\'' + ", size=" + this.size + ", lastModified=" + this.lastModified + ", storageClass='" + this.storageClass + '}';
    }
}
