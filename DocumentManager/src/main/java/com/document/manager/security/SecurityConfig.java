package com.document.manager.security;

import com.document.manager.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] SWAGGER_ENDPOINTS = {
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-ui.html#/**",
                "/webjars/**"
        };

        String[] NON_AUTHENTICATION_ENDPOINT = {
                "/api/user/sign-in",
                "/api/user/sign-up"
        };

        http.csrf()
                .disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(SWAGGER_ENDPOINTS)
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(NON_AUTHENTICATION_ENDPOINT)
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(GET, "/api/user/**").hasAuthority("ROLE_USER")
                .antMatchers(POST, "/api/user/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
