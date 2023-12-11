package com.example.thuchanh_www_week10.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity

public class SercurityConfig {
//    @Autowired
//    public void globalConfig(AuthenticationManagerBuilder auth, PasswordEncoder
//            encoder) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser(User.withUsername("admin")
//                        .password(encoder.encode("admin"))
//                        .roles("ADMIN")
//                        .build())
//                .withUser(User.withUsername("teo")
//                        .password(encoder.encode("teo"))
//                        .roles("TEO")
//                        .build())
//                .withUser(User.withUsername("ty")
//                        .password(encoder.encode("ty"))
//                        .roles("USER")
//                        .build())
//        ;
//    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder encoder, DataSource dataSource) throws Exception{
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser(User.withUsername("user")
                        .password(encoder.encode("user"))
                        .roles("USER"))
                .withUser(
                        User.withUsername("admin")
                                .password(encoder.encode("admin"))
                                .roles("ADMIN","USER")
                );
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/", "/home", "/index").permitAll()//nhung links nay khong can authenticate
//                .requestMatchers("/api/**").hasAnyRole("ADMIN", "USER", "TEO")//nhung uri bat dau bang /api can phai dang nhap voi cac role admin/user/teo
//                .requestMatchers(("/admin/**")).hasRole("ADMIN")//uri bat dau bang/admin thi phai dang nhap voi quyen admin
//                .anyRequest().authenticated()//cac uri khac can dang nhap duoi bat ky role nao
//        );
//        http.httpBasic(Customizer.withDefaults());//cac thiet lap con lai thi theo mac dinh
//        return http.build();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth
                .requestMatchers("/","/home","/index").permitAll()
                .requestMatchers("/api/**").hasAnyRole("ADMIN","USER","NV")
                .requestMatchers(("/admin/**")).hasRole("ADMIN")
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
        );
        http.csrf(csrf->csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
