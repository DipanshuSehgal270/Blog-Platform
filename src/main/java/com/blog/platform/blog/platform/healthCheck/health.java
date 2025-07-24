package com.blog.platform.blog.platform.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class health {

    @GetMapping("/healthCheack")
    public static void healthCheck()
    {
        System.out.println("This is health check controller.");
    }
}
