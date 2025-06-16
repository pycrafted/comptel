package com.comptel.backend;

import com.comptel.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Ce filtre personnalisé valide les tokens JWT dans les requêtes.
 */
@Component
public class AuthentificationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
//    public AuthenticationFilter(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }

    /**
     * Initialise le filtre avec le service JWT.
     * @param jwtService
     */
    public AuthentificationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Vérifie le token JWT dans la requête et authentifie l'utilisateur.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws java.io.IOException

     * Extrait le token JWT de l'en-tête Authorization.
     * Utilise jwtService.getAuthUser pour valider le token et obtenir le username.
     * Crée un UsernamePasswordAuthenticationToken et le stocke dans SecurityContextHolder pour indiquer que l'utilisateur est authentifié.
     * Passe la requête au filtre suivant via filterChain.doFilter.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (token != null && token.startsWith("Bearer ")) {
            String username = jwtService.getAuthUser(request);
            if (username != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/login") || path.equals("/actuator/health");
    }
}
