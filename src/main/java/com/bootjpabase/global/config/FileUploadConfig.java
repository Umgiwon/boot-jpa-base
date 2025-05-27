package com.bootjpabase.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bootjpabase.file.upload")
public class FileUploadConfig {
    private String rootPath;
    private long fileMaxSize;
}
