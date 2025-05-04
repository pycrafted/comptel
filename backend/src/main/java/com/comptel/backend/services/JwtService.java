package com.comptel.backend.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Ce service gère la création et la validation des tokens JWT.
 */
@Component
public class JwtService {
    static final long EXPIRATIONTIME = 86400000;
    static final String PREFIX = "Bearer";
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Génère un token JWT pour un utilisateur donné.
     * @param username
     * @return Le token JWT signé.
     *
     * Crée un token avec le username comme sujet, une expiration de 24 heures, et une signature HS256.
     * Appelé par LoginUserController après une authentification réussie.
     */
    public String getToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur d'un token JWT contenu dans une requête.
     * @param request
     * @return Le nom d'utilisateur extrait du token, ou null si le token est absent ou invalide.
     *
     * Récupère le token depuis l'en-tête Authorization.
     * Valide le token et extrait le username en utilisant la clé secrète.
     * Utilisé par AuthentificationFilter pour identifier l'utilisateur dans les requêtes protégées.
     */
    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody()
                    .getSubject();
            if (user != null)
                return user;
        }
        return null;
    }
}