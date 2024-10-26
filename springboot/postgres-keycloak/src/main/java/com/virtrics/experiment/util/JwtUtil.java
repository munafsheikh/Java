package com.virtrics.experiment.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "your-secret-key";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static String getSecret(String username, String password) throws NoSuchAlgorithmException {
        return getSHA1Hash(username + "," + SECRET + "," + password);

    }

    public static String getSHA1Hash(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        StringBuilder hexString = new StringBuilder();

        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT signature");
            return false;
        }
    }

    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String getAuthorizationToken() {

        return generateJwtToken("Munaf", "TestPassword01"); //getToken("");
    }

    public static String generateJwtToken(String username, String password) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setAudience("web")
                .setIssuer("Issuer-1")
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))

                .signWith(SignatureAlgorithm.HS512, SECRET)

                .compact();
    }

    private static String getToken(String s) {
        return "Bearer " + s;
    }
}