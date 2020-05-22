package com.gk.JDBCSpringSecurity.JDBCAuthentication.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

import javax.sql.DataSource;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Defining an data source for telling how our database is structured.
    // We need to have a Configuration somewhere else and then inject it here.
    // Now because we are using h2 database we don't need to configure the db because it is already embedded in spring.
    @Autowired
    DataSource dataSource;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema() // creating the default db schema
                .withUser(
                        User.withUsername("user")
                        .password("pass")
                        .roles("USER")
                )
                .withUser(
                        User.withUsername("admin")
                                .password("pass")
                                .roles("ADMIN")
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Most Restrictive To Least Restrictive in terms of order
                .antMatchers("static/css", "static/js").permitAll() //Static files permit for all users
                .antMatchers("/admin").hasRole("ADMIN") // .hasAnyRole("USER","ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/").permitAll()
                .and().formLogin();
    }
}
