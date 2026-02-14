package com.sagar.resume.Util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String userid) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(userid)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token)
    {
       try
       {
           Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token);
           return  true;
       }
       catch (JwtException | IllegalArgumentException e)
       {
         return false;
       }

    }

    public boolean isTokenExpired(String token)
    {
        try
        {
             Date expiration =Jwts.parserBuilder()
                                    .setSigningKey(getSigningKey())
                                    .build()
                                    .parseClaimsJws(token)
                                    .getBody()
                                    .getExpiration();

             return expiration.before(new Date());

        }
        catch (JwtException | IllegalArgumentException e)
        {
            return true;
        }

    }

    public Key getSigningKey()
    {
       return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}