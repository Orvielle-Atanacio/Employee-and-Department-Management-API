package com.luv2code.springboot.cruddemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;


// This service handles all JWT token operations - creating, validating, and extracting data from tokens
@Service  // Marks this as a Spring service bean that can be injected elsewhere
public class JwtService {

    // Injects the secret key from application.properties file
    // This key is used to sign and verify JWT tokens
    @Value("${jwt.secret}")
    private String secretKey;

    // Injects token expiration time from application.properties (in milliseconds)
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Extracts the username (subject) from the JWT token
    public String extractUsername(String token) {
        // Uses Claims::getSubject to get the "sub" claim which typically stores the username
        return extractClaim(token, Claims::getSubject);
    }

    // Generic method to extract any claim from the token using a function
    // This is a helper method that other extraction methods use
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // First, extract all claims from the token
        final Claims claims = extractAllClaims(token);
        // Then apply the specific function to get the desired claim (username, expiration, etc.)
        return claimsResolver.apply(claims);
    }

    // Generates a JWT token for a user with no extra claims
    public String generateToken(UserDetails userDetails) {
        // Calls the main generateToken method with an empty map for extra claims
        return generateToken(new HashMap<>(), userDetails);
    }

    // Main method to generate a JWT token with optional extra claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Uses the Jwts builder to construct the token step by step
        return Jwts.builder()
                .setClaims(extraClaims)                    // Set any additional claims (roles, permissions, etc.)
                .setSubject(userDetails.getUsername())     // Set the subject (usually username)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set when token was created
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Set expiration time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sign with our secret key using HS256 algorithm
                .compact();  // Finally build and return the compact string representation
    }

    // Validates if a token is valid for a specific user
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Extract username from token
        final String username = extractUsername(token);
        // Check if: 1) username matches, AND 2) token is not expired
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Checks if the token has expired
    private boolean isTokenExpired(String token) {
        // Compare token expiration time with current time
        return extractExpiration(token).before(new Date());
    }

    // Extracts the expiration date from the token
    private Date extractExpiration(String token) {
        // Uses Claims::getExpiration to get the "exp" claim
        return extractClaim(token, Claims::getExpiration);
    }

    // Extracts all claims (payload data) from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()           // Start building a parser
                .setSigningKey(getSignInKey()) // Set the key to verify the token signature
                .build()                      // Build the parser
                .parseClaimsJws(token)        // Parse the token and verify its signature
                .getBody();                   // Return the claims (payload) part of the token
    }

    // Converts the base64-encoded secret key into a Key object for signing/verification
    private Key getSignInKey() {
        // Decode the base64 secret key string into bytes
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Create a Key object suitable for HMAC-SHA algorithms
        return Keys.hmacShaKeyFor(keyBytes);
    }
}