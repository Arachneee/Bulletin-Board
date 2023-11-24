package com.arachneee.bulletinboard.security.config;

import com.arachneee.bulletinboard.security.provider.CustomAuthenticationProvider;
import com.arachneee.bulletinboard.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 메모리 방식
//    @Bean
//    public UserDetailsService userDetailsManager(AuthenticationConfiguration authenticationConfiguration) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("1111"))
//                .roles("USER")
//                .build();
//        UserDetails manager = User.builder()
//                .username("manager")
//                .password(passwordEncoder().encode("1111"))
//                .roles("MANAGER", "USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("1111"))
//                .roles("ADMIN", "USER", "MANAGER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, manager, admin);
//    }

    // 패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new MvcRequestMatcher(introspector, "/"), new MvcRequestMatcher(introspector, "/posts"), new MvcRequestMatcher(introspector, "/members/add")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/posts/[0-9]+")).hasRole("USER")
                        .requestMatchers(new MvcRequestMatcher(introspector, "/posts/add")).hasRole("MANAGER")
                        .requestMatchers(new MvcRequestMatcher(introspector, "/posts/[0-9]+/edit")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults());

        return http.build();
    }

    // 정적 파일 보안 제거
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }


}
