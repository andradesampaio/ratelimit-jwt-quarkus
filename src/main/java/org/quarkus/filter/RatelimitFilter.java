package org.quarkus.filter;

import io.quarkus.redis.client.RedisClient;
import io.vertx.core.http.HttpServerRequest;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.redis.client.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Intercept
@Provider
public class RatelimitFilter implements ContainerRequestFilter
{
    @Context
    HttpServerRequest httpRequest;
    @Context
    HttpServerResponse httpResponse;

    @Inject
    RedisClient redisClient;

    @ConfigProperty(name = "requests.per.minute", defaultValue = "15")
    int customLimit;

    private final static Logger LOGGER = Logger.getLogger(RatelimitFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext context){
        final LocalDateTime now = LocalDateTime.now();
        var remoteAddress = httpRequest.remoteAddress().toString();

        Response requests = redisClient.get(remoteAddress);
        int requestNo = (requests != null) ? requests.toInteger(): 0;

        if (requestNo >= customLimit) {
            context.abortWith(javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.TOO_MANY_REQUESTS)
                    .header("X-Rate-Limit-Retry-After-Seconds", 60 - now.getSecond())
                    .build());
            return;
        }

        redisClient.multi();
        redisClient.incr(remoteAddress);
        redisClient.expire(remoteAddress, "60");

        int limite = customLimit - requestNo - 1;
        LOGGER.finest(String.format("Request count is %s ", redisClient.exec()));

        httpResponse.putHeader("X-Rate-Limit-Remaining",String.valueOf(limite));
    }
}
