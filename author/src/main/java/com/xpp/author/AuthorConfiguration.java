package com.xpp.author;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.author")
@Component
@Data
public class AuthorConfiguration {
    private String name;
}
