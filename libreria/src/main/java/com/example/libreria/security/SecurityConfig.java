package com.example.libreria.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.libreria.repositoryes.UtenteRepository;
import com.example.libreria.models.Utente;

import java.util.Collections;
import java.util.List;

/**
 * Configurazione di Spring Security per gestire l'autenticazione e
 * l'autorizzazione.
 */
@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    private final UtenteRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Utente user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    
            // Convertiamo il ruolo enum in un oggetto GrantedAuthority compatibile con Spring Security
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRuolo().name())
            );
    
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),   // Email come username
                    user.getPassword(), // Password cifrata
                    authorities        // Ruoli/permessi
            );
        };
    }
    

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for simplicity (consider enabling it in production)
                .csrf(csrf -> csrf.disable())

                // Configure access control
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**").permitAll() 
                        .requestMatchers("/libri").permitAll() 
                        .requestMatchers("/admin/**").hasRole("ADMIN") 
                        .anyRequest().authenticated())

                // Configure the login form
                .formLogin(form -> form
                        .loginPage("/auth/login") 
                        .loginProcessingUrl("/auth/login") // URL to process login form submission
                        .successHandler((request, response, authentication) -> {
                            // Redirect based on the role of the authenticated user
                            String targetUrl = "/user/lista"; // Default redirect target

                            if (authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                                targetUrl = "/admin/libri"; // Redirect to /admin/eventi for ADMIN
                            }
                        
                            response.sendRedirect(targetUrl);
                        })
                        .permitAll()) // Allow everyone to access the login form

                // Configure logout functionality
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // URL to trigger logout
                        .logoutSuccessUrl("/eventi?logout") // Redirect to /eventi after successful logout
                        .permitAll()) // Allow everyone to access the logout URL

                // Configure authentication provider
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

}
