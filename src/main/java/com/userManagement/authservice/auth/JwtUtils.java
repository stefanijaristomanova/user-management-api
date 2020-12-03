package com.userManagement.authservice.auth;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";
    private static final String AUTHORITIES_PARAM = "Authorities";



    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationInMs}")
    private int expirationInMs;

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities ) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_PARAM, authorities);
        return doGenerateToken(claims, email);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    public Set<GrantedAuthority> getAuthoritiesFromToken(String token) {

        List<LinkedHashMap<String, String>> authorities = getClaimFromToken(token, AUTHORITIES_PARAM);

        return authorities.stream()
                .flatMap(map -> map.values().stream())
                .map(roleName -> new SimpleGrantedAuthority(roleName))
                .collect(Collectors.toSet());
    }

    public Boolean isTokenExpired(String token) {

        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {

        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public <T> T getClaimFromToken(String token, String attribute) {

        final Claims claims = getAllClaimsFromToken(token);
        return (T) claims.get(attribute);
    }

    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest req) {

        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}