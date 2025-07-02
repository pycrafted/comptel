package com.comptel.backend;

import com.comptel.backend.services.UseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Cette classe configure Spring Security pour l'authentification et l'autorisation.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UseImpl userDetailsService;
    private final AuthentificationFilter authenticationFilter;

    /**
     * Initialise la configuration avec les dépendances nécessaires.
     * @param userDetailsService Service pour charger les utilisateurs (UseImpl).
     * @param authenticationFilter Filtre pour valider les tokens JWT.
     *
     * Configure la classe pour utiliser UseImpl et AuthentificationFilter.
     */
    public SecurityConfig(UseImpl userDetailsService, AuthentificationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
    }

    /**
     * Configure les règles de sécurité pour l'application.
     * @param http Objet HttpSecurity pour définir les configurations de sécurité.
     * @return SecurityFilterChain - La chaîne de filtres de sécurité configurée.
     * @throws Exception
     *
     * Active CORS avec une configuration personnalisée.
     * Exige l'authentification pour les endpoints /api/**, autorise tout le reste.
     * Ajoute AuthentificationFilter avant le filtre standard de Spring Security.
     * Active l'authentification HTTP Basic (optionnel).
     * Configure l'endpoint /api/logout pour la déconnexion.
     * Désactive CSRF pour une application stateless.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/services/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(401);
                    response.getWriter().write("{\"error\": \"Non authentifié\", \"message\": \"" + authException.getMessage() + "\"}");
                })
            );

        return http.build();
    }

    /**
     *  Fournit un gestionnaire d'authentification pour valider les identifiants.
     * @param authConfig Configuration d'authentification Spring.
     * @return AuthenticationManager Gestionnaire pour authentifier les utilisateurs.
     * @throws Exception
     *
     * C'est pour vérifier les identifiants lors de la connexion.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Fournit un encodeur de mots de passe pour hacher les mots de passe.
     * @return PasswordEncoder - Instance de BCryptPasswordEncoder.
     *
     * Utilisé pour hacher les mots de passe avant de les stocker et pour vérifier les mots de passe saisis.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure CORS pour permettre l'accès depuis un frontend.
     * @return CorsConfigurationSource - Configuration CORS pour toutes les requêtes.
     *
     *
     * Autorise toutes les méthodes HTTP et en-têtes.
     * Expose l'en-tête Authorization pour le frontend.
     * Active les credentials pour supporter les requêtes authentifiées.
     * Applique la configuration à tous les endpoints (/**).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(java.util.Arrays.asList(
            "http://localhost:3000",
            "https://comptel-frontend.onrender.com"
        ));
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.Arrays.asList("*"));
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Content-Type");
        configuration.addExposedHeader("Accept");
        configuration.addExposedHeader("Origin");
        configuration.addExposedHeader("Access-Control-Request-Method");
        configuration.addExposedHeader("Access-Control-Request-Headers");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}