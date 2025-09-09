// package com.Healthcare.config;

// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.*;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.*;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.*;
// import com.Healthcare.security.JwtAuthenticationFilter;

// @Configuration
// public class SecurityConfig {
//     @Autowired private JwtAuthenticationFilter jwtAuthFilter;

//     @Bean
//     public BCryptPasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.cors().and().csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/api/auth/**").permitAll()
//                 .requestMatchers("/api/duty-roster/**").permitAll()
//                 .requestMatchers("/api/doctor/**").permitAll()
//                 .requestMatchers("/api/specialization/**").permitAll()
//                 .requestMatchers("/api/appointments/**").permitAll()
//                 .requestMatchers("/api/states/**").permitAll()
//                 .requestMatchers("/uploads/**").permitAll() 
//                 .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                 .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                 .anyRequest().hasAnyRole("USER","ADMIN")
                
//             )
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//         http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//         return http.build();
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration cfg = new CorsConfiguration();
//         // cfg.setAllowedOrigins(List.of("https://novacare-backend.onrender.com"));
//         // cfg.setAllowedOrigins(List.of("https://novacare-frontend-healthcare.vercel.app"));
//        cfg.setAllowedOrigins(List.of(
//             "https://novacare-frontend-healthcare.vercel.app",
//             "https://novacare-healthcare.vercel.app",
//             "http://localhost:3000"
//         ));
//         cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
//         cfg.setAllowedHeaders(List.of("*"));
//         cfg.setAllowCredentials(true);
//         cfg.setMaxAge(3600L);
//         UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
//         src.registerCorsConfiguration("/**", cfg);
//         return src;
//     }
// }

package com.Healthcare.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;                       // <--- UPDATED: Import CorsFilter
import org.springframework.core.Ordered;                            // <--- UPDATED: Import Ordered
import org.springframework.core.annotation.Order;                  // <--- UPDATED: Import Order annotation

import com.Healthcare.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/duty-roster/**").permitAll()
                .requestMatchers("/api/doctor/**").permitAll()
                .requestMatchers("/api/specialization/**").permitAll()
                .requestMatchers("/api/appointments/**").permitAll()
                .requestMatchers("/api/states/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().hasAnyRole("USER", "ADMIN")
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(                                 // <--- UPDATED Allowed Origins
            "https://novacare-frontend-healthcare.vercel.app",
            "https://novacare-healthcare.vercel.app",
            "http://localhost:3000"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // <--- UPDATED Allowed Methods
        cfg.setAllowedHeaders(List.of("*"));                                         // <--- UPDATED Allowed Headers
        cfg.setAllowCredentials(true);                                               // <--- UPDATED Allow Credentials
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // <--- ADDED Global CorsFilter Bean to handle CORS with highest precedence
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)                                        // <--- UPDATED: Set highest precedence
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(                                     // <--- UPDATED Allowed Origins
            "https://novacare-frontend-healthcare.vercel.app",
            "https://novacare-healthcare.vercel.app",
            "http://localhost:3000"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // <--- UPDATED Allowed Methods
        config.setAllowedHeaders(List.of("*"));                                        // <--- UPDATED Allowed Headers
        config.setAllowCredentials(true);                                              // <--- UPDATED Allow Credentials
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

