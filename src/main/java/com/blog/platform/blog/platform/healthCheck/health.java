package com.blog.platform.blog.platform.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class health {

    @GetMapping("/healthCheck")
    public static String healthCheck()
    {
        return "health controller running fine";
    }
}
