package com.example.filedemo.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String processedDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
    
    public String getProcessedDir() {
        return processedDir;
    }

    public void setProcessedDir(String processedDir) {
        this.processedDir = processedDir;
    }
}
