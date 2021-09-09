package com.xpp.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    @Autowired
    private AuthorFeignClient authorFeignClient;
    public @GetMapping("getBookAuthorName") String getBookAuthorName(){
        return authorFeignClient.getAuthorName();
    }
}
