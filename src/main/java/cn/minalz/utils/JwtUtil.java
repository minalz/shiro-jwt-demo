package cn.minalz.utils;

import cn.minalz.config.redis.RedisConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 */
public class JwtUtil {
    static final String SECRET = "ThisIsASecret";

    public static String generateToken(HashMap<String, Object> map) {
        String jwt = Jwts.builder()
                .setClaims(map)
//                .setExpiration(new Date(System.currentTimeMillis() + 3600_000_000L))// 1000 hour
                .setExpiration(new Date(System.currentTimeMillis() + RedisConstant.TOKEN_STORAGE_USERNAME_TIME)) // 和Redis中的时间保持一致
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return "Bearer " + jwt; //jwt前面一般都会加Bearer
    }

    public static Map<String, Object> validateToken(String token) {
        try {
            // parse the token.
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            return body;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token. " + e.getMessage());
        }
    }

    /**
     * 获得token中的信息无需password解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        // parse the token.
        Map<String, Object> body = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
        Object username = body.get("username");
        if (username != null) {
            return username.toString();
        }
        return null;
    }
}
