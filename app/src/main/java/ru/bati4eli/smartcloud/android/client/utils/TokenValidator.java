package ru.bati4eli.smartcloud.android.client.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class TokenValidator {
    private static final String secretKey = "123412341234123412341234888877771234123412341234123412348888777712341234123412341234123488887777";
    public static boolean isTokenExpired(String token) {
        if (token == null) {
            return true;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
