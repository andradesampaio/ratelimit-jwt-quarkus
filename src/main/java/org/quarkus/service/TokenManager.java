package org.quarkus.service;

import io.smallrye.jwt.build.Jwt;
import io.vertx.core.http.HttpServerRequest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.quarkus.model.User;
import org.quarkus.resource.AuthenticationResponse;

import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Singleton
public class TokenManager {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "mp.jwt.expiration")
    Long expirationInMillis;

    static Long currentTimeInSecs = System.currentTimeMillis() / 1000l;

    public Response createSuccessLoginResponse(final User user, final HttpServerRequest httpServerRequest){
        String token = getToken(user);
        return Response.ok(new AuthenticationResponse(user,token))
                .header(HttpHeaders.AUTHORIZATION, token)
                .expires(Date.from(Instant.now().plus(Duration.ofMinutes(1))))
                .cookie(new NewCookie("auth-token", token, "/", httpServerRequest.remoteAddress().hostName(),
                        null, 60 * 30, false)).build();
    }

    public Response createFailedLoginResponse() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private String getToken(User user){
        return  Jwt.issuer(issuer)
                .subject(("id_"+UUID.randomUUID()))
                .audience("jwt-token")
                .groups(user.role)
                .claim("access_token", UUID.randomUUID())
                .issuedAt(currentTimeInSecs)
                .expiresIn(expirationInMillis)
                .sign();
    }

}
