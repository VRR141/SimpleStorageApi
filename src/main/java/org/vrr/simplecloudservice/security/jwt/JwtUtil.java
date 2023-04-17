package org.vrr.simplecloudservice.security.jwt;

public interface JwtUtil {

    String generateToken(String uuid);

    String validateTokenAndRetrieveClaim(String token);

    String getUuidFromToken(String token);
}
