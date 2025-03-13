package com.bootjpabase.global.file.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bootjpabase.file.upload")
public class FileUploadProperties {
    private String rootPath;
    private long fileMaxSize;
}
