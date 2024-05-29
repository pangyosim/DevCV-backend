package com.devcv.member.Presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class memberController {

    @GetMapping("/restDocsTest")
    public String restDocsTestAPI() {
        return "test!!";
    }
}
