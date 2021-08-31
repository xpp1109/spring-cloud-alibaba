package com.xpp.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RefreshScope
public class AuthorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorApplication.class, args);
    }

    @Value("${spring.author.name}")
    private String authorName;

    @Autowired
    private AuthorConfiguration authorConfiguration;

    @GetMapping("/getAuthorName")
    public String getAuthorName() {
        return this.authorName + "\n" + "通过AuthorConfiguration读取结果:" + authorConfiguration.getName();
    }
}
