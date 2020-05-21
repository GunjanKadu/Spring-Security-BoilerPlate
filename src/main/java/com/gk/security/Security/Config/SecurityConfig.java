package com.gk.security.Security.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//Because we are dealing with request and response we need to use enable web security we can also use #MethodSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set Your Configuraton to the auth object
        auth.inMemoryAuthentication()
                //Method Chaining
                .withUser("blah")
                .password("blah")
                .roles("USER")
                .and()
                .withUser("blah1")
                .password("blah1")
                .roles("ADMIN");
    }

    // Password encode to store password in hashed format as required by security.
    // But now we are just storing normal password for learning purpose
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}
