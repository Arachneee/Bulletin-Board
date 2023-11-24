package com.arachneee.bulletinboard.security.config;

import com.arachneee.bulletinboard.security.handler.CustomAccessDeniedHandler;
import com.arachneee.bulletinboard.security.handler.CustomAuthenticationSuccessHandler;
import com.arachneee.bulletinboard.security.provider.CustomAuthenticationProvider;
import com.arachneee.bulletinboard.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationDetailsSource authenticationDetailsSource;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//                        .requestMatchers(regexMatcher("/posts/[0-9]+/edit*")).hasAuthority("ADMIN")
//                        .requestMatchers(regexMatcher("/posts/[0-9]+?[]*")).hasAuthority("USER")
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/posts").authenticated()
                        .requestMatchers("/posts/add").hasAuthority("MANAGER")
                        .requestMatchers("/posts/{id}").hasAuthority("USER")
                        .requestMatchers("/posts/**").hasAuthority("ADMIN")
                        .requestMatchers("/members/add").permitAll()
                        .requestMatchers("/login**").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(withDefaults()) // defaults 옵션
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login_proc")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .authenticationDetailsSource(authenticationDetailsSource) // 추가 데이터 전달
                        .successHandler(authenticationSuccessHandler) // 인증 성공 후 처리
                        .failureHandler(authenticationFailureHandler) // 인증 실패 처리
                        .permitAll())
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler())) // 인가 예외 처리
        ;

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }


    // 정적 파일 보안 제거
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }


}
