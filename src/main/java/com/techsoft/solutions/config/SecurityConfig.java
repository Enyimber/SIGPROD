package com.techsoft.solutions.config;

import com.techsoft.solutions.service.UsuarioDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableAsync   // habilita @Async para BitacoraService
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ── CSRF: desactivar solo para rutas REST API ────────────────
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

            .authorizeHttpRequests(auth -> auth
                // Públicas
                .requestMatchers("/auth/**", "/css/**", "/js/**", "/img/**",
                                 "/webjars/**", "/favicon.ico").permitAll()

                // API REST — cualquier usuario autenticado
                .requestMatchers("/api/**").authenticated()

                // Administración
                .requestMatchers("/usuarios/**").hasRole("ADMIN")

                // Backlog — Product Owner, PM, Analista, Admin
                .requestMatchers("/backlog/**")
                    .hasAnyRole("ADMIN","PRODUCT_OWNER","PROJECT_MANAGER","ANALISTA")

                // Sprints — todos los roles del equipo
                .requestMatchers("/sprints/**")
                    .hasAnyRole("ADMIN","PROJECT_MANAGER","PRODUCT_OWNER","ANALISTA",
                                "ARQUITECTO","FRONTEND","BACKEND","QA","DEVOPS")

                // Defectos — todos los roles del equipo
                .requestMatchers("/defectos/**")
                    .hasAnyRole("ADMIN","QA","FRONTEND","BACKEND","DEVOPS",
                                "PROJECT_MANAGER","PRODUCT_OWNER","ANALISTA","ARQUITECTO")

                // Riesgos — todos los roles del equipo
                .requestMatchers("/riesgos/**")
                    .hasAnyRole("ADMIN","PROJECT_MANAGER","ANALISTA","ARQUITECTO",
                                "PRODUCT_OWNER","FRONTEND","BACKEND","QA","DEVOPS")

                // Proyectos — todos los roles
                .requestMatchers("/proyectos/**")
                    .hasAnyRole("ADMIN","PROJECT_MANAGER","PRODUCT_OWNER","ANALISTA",
                                "ARQUITECTO","FRONTEND","BACKEND","QA","DEVOPS")

                // Tareas — todos los roles
                .requestMatchers("/tareas/**")
                    .hasAnyRole("ADMIN","PROJECT_MANAGER","PRODUCT_OWNER","ANALISTA",
                                "ARQUITECTO","FRONTEND","BACKEND","QA","DEVOPS")

                // Dashboard — cualquier autenticado
                .requestMatchers("/dashboard/**").authenticated()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/auth/login?error=true")
                .usernameParameter("correo")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .rememberMe(rem -> rem
                .key("techsoft-sigprod-remember-me-2026")
                .tokenValiditySeconds(86400)
            )
            // Manejo de acceso denegado
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/auth/login?denied=true")
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(usuarioDetailsService)
               .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
