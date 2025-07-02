package com.comptel.backend.controllers;

import com.comptel.backend.domain.AccountCredentials;
import com.comptel.backend.services.JwtService;
import com.comptel.backend.services.UseImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

/**
 * Ce contrôleur gère l'authentification via l'endpoint /login.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "https://comptel-frontend.onrender.com"}, allowCredentials = "true")
public class LoginUserController {
    private static final Logger logger = LoggerFactory.getLogger(LoginUserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UseImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        logger.info("Tentative de connexion reçue pour l'utilisateur: {}", credentials.username());
        logger.debug("Détails de la requête - Username: {}, Password length: {}", 
            credentials.username(), 
            credentials.password() != null ? credentials.password().length() : 0);

        if (credentials.username() == null || credentials.password() == null) {
            logger.error("Tentative de connexion avec des credentials manquants - Username: {}, Password: {}", 
                credentials.username() == null ? "null" : "présent",
                credentials.password() == null ? "null" : "présent");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Identifiants invalides: nom d'utilisateur et mot de passe requis");
        }

        try {
            logger.debug("Tentative d'authentification avec les credentials");
            // Ne pas encoder le mot de passe avant l'authentification
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password())
            );
            
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.info("Authentification réussie pour l'utilisateur: {}", credentials.username());
            
            String token = jwtService.getToken(credentials.username());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", credentials.username());
            response.put("role", userService.loadUserByUsername(credentials.username()).getAuthorities());
            response.put("id", userService.getUserIdByUsername(credentials.username()));
            
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("Échec de l'authentification pour l'utilisateur {}: {}", 
                credentials.username(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Identifiants invalides: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'authentification: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur serveur: " + e.getMessage());
        }
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtService.validateToken(token)) {
                String username = jwtService.getUsernameFromToken(token);
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);
                response.put("role", userService.loadUserByUsername(username).getAuthorities());
                response.put("id", userService.getUserIdByUsername(username));
                return ResponseEntity.ok().body(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false));
    }
}
