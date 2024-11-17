package com.ganzithon.Hexfarming.utility;

import com.ganzithon.Hexfarming.domain.user.CustomUserDetails;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtManager {
    @Value("${jwt.accessExpiration}") // application.properties에 작성한 jwt.accessExpiration 값을 가져옴
    private long accessExpiration;
    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String createToken(int userId, boolean isRefresh) {
        Map<String, Integer> claim = createClaim(userId);

        Date now = new Date();
        Date expireTime = new Date(now.getTime() + accessExpiration); // 액세스 토큰의 만료 시간
        if (isRefresh) {
            expireTime = new Date(now.getTime() + refreshExpiration); // 리프레시 토큰의 만료 시간
        }

        Key key = getSignKey();
        String token = Jwts.builder() // Jwt 토큰을 생성
                .setClaims(claim)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public Map<String, Integer> createClaim(int userId) {
        Map<String, Integer> claim = new HashMap<>();
        claim.put("userId", userId);
        return claim;
    }

    public Key getSignKey() { // 토큰 생성에 사용할 키를 Key 객체로 만듦
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8); // 암호화 키를 바이트 배열로 분해
        return Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA512 알고리즘에 사용할 키 생성
    }

    public int validateToken(String token) {
        try {
            JwtParser jwtParser = getJwtParser();
            return (int) jwtParser.parseSignedClaims(token).getPayload().get("userId"); // 토큰 검증 후 userId 반환
        } catch (MalformedJwtException exception) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), exception.getMessage());
        }
    }

    public JwtParser getJwtParser() {
        Key key = getSignKey();
        return Jwts.parser().verifyWith((SecretKey) key).build();
    }

    public Authentication getAuthentication(CustomUserDetails customUserDetails) {
        return new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
    }
}
