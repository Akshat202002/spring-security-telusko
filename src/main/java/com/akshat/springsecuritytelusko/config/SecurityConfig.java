package com.akshat.springsecuritytelusko.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // This method defines a bean for configuring the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Disabling Cross-Site Request Forgery (CSRF) Protection
        // CSRF protection helps prevent unauthorized commands from being transmitted
        // from a trusted user. It is typically essential for web applications that
        // utilize session-based authentication. However, you might consider disabling
        // it for stateless applications, such as APIs that use tokens.

        // CSRF can be disabled using three approaches:
        // 1. Using AbstractHttpConfigurer::disable (uncomment to use).
        // 2. Using an imperative customizer approach (provided below).
        // 3. Using a lambda expression (shown in the next line).

        // Uncomment the following method to disable CSRF using AbstractHttpConfigurer:
        // http.csrf(AbstractHttpConfigurer::disable);

        // Imperative way to disable CSRF using a customizer
        /*
        Customizer<CsrfConfigurer<HttpSecurity>> custCsrf = new Customizer<CsrfConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CsrfConfigurer<HttpSecurity> customizer) {
                customizer.disable();
            }
        };
        http.csrf(custCsrf);
        */

        // Disable CSRF protection using a lambda for clarity and conciseness
        http.csrf(customizer -> customizer.disable());

        // Configuring Authorization Rules
        // This line ensures that every request made to the application must be authenticated.
        // Without proper authentication, users will be denied access.
        http.authorizeHttpRequests(request ->
                request.anyRequest().authenticated() // Enforce authentication for all requests
        );

        // Configuring Form-Based Login
        // This line enables the default Spring Security login mechanism, which allows users
        // to authenticate via a login form provided by Spring Security.
        http.formLogin(Customizer.withDefaults());

        // Configuring HTTP Basic Authentication
        // This line enables Basic authentication, which is useful for API clients like Postman.
        // It prevents the application from returning an HTML login form when a protected
        // resource is accessed using Basic authentication.
        http.httpBasic(Customizer.withDefaults());

        // Configure Stateless Session Management
        // By setting the session creation policy to stateless, we ensure that the application
        // does not maintain session information between requests. Each request will be treated
        // independently, which is common in RESTful APIs.
        // Note: Stateless session management disables form login, as each request generates a new session.
        // This is ideal for API use but may require different handling for traditional web applications.
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Build and return the configured SecurityFilterChain object
        // This method constructs the SecurityFilterChain, containing all the above configurations.
        return http.build();


        // Or using builder pattern
        /* return http
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request ->
                request.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build(); */

    }

    // Without any database
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("demo")
//                .password("d@123")
//                .roles("USER")
//                .build();
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

    // We have to use our own authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}