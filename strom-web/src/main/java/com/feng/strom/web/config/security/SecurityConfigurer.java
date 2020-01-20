
package com.feng.strom.web.config.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * @author
 */
@EnableWebSecurity
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        /*忽略swagger相关页面*/
        web.ignoring().antMatchers(
                "/swagger-ui.html",
                "/v2/**",
                "/swagger-resources/**",
                "/test/**",
                "/kafka/**");
    }
}
