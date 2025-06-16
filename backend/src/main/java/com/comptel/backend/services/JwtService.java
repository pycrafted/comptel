package com.comptel.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Ce service gère la création et la validation des tokens JWT.
 */
@Component
public class JwtService {
    static final long EXPIRATIONTIME = 86400000; // 24 heures
    static final String PREFIX = "Bearer";
    // Clé secrète fixe encodée en base64
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));

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
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(key)
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
        if (token != null && token.startsWith(PREFIX)) {
            try {
                String tokenValue = token.replace(PREFIX, "").trim();
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(tokenValue)
                        .getBody();

                // Vérifier si le token n'est pas expiré
                if (claims.getExpiration().before(new Date())) {
                    return null;
                }

                return claims.getSubject();
            } catch (Exception e) {
                System.err.println("Erreur lors de la validation du token: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrait le nom d'utilisateur d'un token JWT.
     * @param token Le token JWT
     * @return Le nom d'utilisateur extrait du token
     * @throws Exception Si le token est invalide ou expiré
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

            // Vérifier si le token n'est pas expiré
            if (claims.getExpiration().before(new Date())) {
                throw new Exception("Token expiré");
            }

            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'extraction du nom d'utilisateur du token: " + e.getMessage());
        }
    }
}