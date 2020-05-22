package com.gk.security.Security.Rest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {


    @GetMapping("/")
    public String welcome() {
        return "Welcome";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin";
    }

}
