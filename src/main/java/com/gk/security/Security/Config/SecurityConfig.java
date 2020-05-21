package com.gk.security.Security.Config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


//Because we are dealing with request and response we need to use enable web security we can also use #MethodSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set Your Configuraton to the auth object
        auth.inMemoryAuthentication()
                .withUser("blah")
                .password("blah")
                .roles("USER");
    }
}
