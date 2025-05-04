package com.comptel.backend.controllers;

import com.comptel.backend.domain.AccountCredentials;
import com.comptel.backend.services.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ce contrôleur gère l'authentification via l'endpoint /login.
 */
@RestController
public class LoginUserController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     *  Initialise le contrôleur avec les dépendances nécessaires.
     *  Configure le contrôleur pour utiliser JwtService et AuthenticationManager
     * @param jwtService
     * @param authenticationManager
     */
    public LoginUserController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authentifie un utilisateur et renvoie un token JWT
     * Crée un UsernamePasswordAuthenticationToken avec les identifiants.
     * Valide les identifiants via authenticationManager.
     * Génère un token JWT avec jwtService si l'authentification réussit.
     * Retourne une réponse avec le token dans l'en-tête Authorization (format : Bearer <token>).
     * @param credentials
     * @return Réponse HTTP avec le token JWT dans l'en-tête Authorization
     */
    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        Authentication auth = authenticationManager.authenticate(creds);
        // Générer le JWT
        String jwts = jwtService.getToken(auth.getName());
        // Construire la réponse avec le JWT dans l'en-tête Authorization
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }
}
