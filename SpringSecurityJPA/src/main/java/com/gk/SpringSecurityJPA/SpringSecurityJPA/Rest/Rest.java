package com.gk.SpringSecurityJPA.SpringSecurityJPA.Rest;

import org.springframework.web.bind.annotation.RequestMapping;

public class Rest {

    @RequestMapping("/")
    public String welcome() {
        return "Hello Welcome";
    }

    @RequestMapping("/user")
    public String welcomeUser() {
        return "Hello Welcome User";
    }

    @RequestMapping("/admin")
    public String welcomeAdmin() {
        return "Hello Welcome Admin";
    }

}
