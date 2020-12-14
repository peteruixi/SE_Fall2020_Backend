package com.healthMonitor.fall2020.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
Written by: Ruixi Li
Tested by: Ruixi Li
Debugged by: Ruixi Li
 */

@Component
public class JwtTokenUtil implements Serializable {
    public static  void main(String [] args)
    {
        JwtTokenUtil util=new JwtTokenUtil();
        String token=util.generateToken("11");

        String userId=util.getUserIdFromToken(token);



        boolean b=util.isTokenExpired(token);
        System.out.println(token);
        System.out.println(userId);
        System.out.println(b);

    }

    private static final String CLAIM_KEY_USERNAME = "sub";

    /**
     * 10天(毫秒)
     */
    private static final long EXPIRATION_TIME = 864000000;
    /**
     * JWT密码
     */
    private static final String SECRET = "fall2020group2";

    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USERNAME, userId);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Boolean validateToken(String token, String userId) {

        String username = getUserIdFromToken(token);

        return (username.equals(userId) && !isTokenExpired(token));
    }

    public String getUserIdFromToken(String token) {
        String userId = getClaimsFromToken(token).getSubject();
        return userId;
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}
