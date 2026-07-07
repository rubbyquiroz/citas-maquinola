package com.clinica.config;

import com.clinica.service.UsuarioService;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioService usuarioService;

    public SecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var pacienteOpt = usuarioService.findPacienteByEmail(username);
            if (pacienteOpt.isPresent()) {
                var paciente = pacienteOpt.get();
                return User.builder()
                        .username(paciente.getEmail())
                        .password(paciente.getUsuario().getPassword())
                        .roles("PACIENTE")
                        .build();
            }

            var doctorOpt = usuarioService.findDoctorByEmail(username);
            if (doctorOpt.isPresent()) {
                var doctor = doctorOpt.get();
                return User.builder()
                        .username(doctor.getEmail())
                        .password(doctor.getUsuario().getPassword())
                        .roles("DOCTOR")
                        .build();
            }

            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/", "/signin", "/do-login", "/resources/**").permitAll()
                        .requestMatchers("/error", "/acceso-denegado").permitAll()
                        .requestMatchers("/paciente/**").hasRole("PACIENTE")
                        .requestMatchers("/doctor/**").hasRole("DOCTOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/signin")
                        .loginProcessingUrl("/do-login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(this::authenticationSuccessHandler)
                        .failureUrl("/signin?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/signin?logout=true")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }

    private void authenticationSuccessHandler(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            Authentication authentication) throws java.io.IOException {

        String redirectUrl = "/";

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"))) {
            redirectUrl = "/paciente/dashboard";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            redirectUrl = "/doctor/dashboard";
        }

        response.sendRedirect(redirectUrl);
    }
}
