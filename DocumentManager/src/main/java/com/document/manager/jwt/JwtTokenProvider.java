package com.document.manager.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String JWT_SECRET = "KLTN_2021";
    private final long JWT_EXPIRATION = 604800000L;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            if (StringUtils.hasText(authToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
