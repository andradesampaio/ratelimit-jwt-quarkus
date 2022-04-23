package org.quarkus.resource;

import io.vertx.core.http.HttpServerRequest;
import org.quarkus.model.dto.UserDTO;
import org.quarkus.model.User;
import org.quarkus.filter.Intercept;
import org.quarkus.service.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import static org.quarkus.model.User.verifyPassword;

@Path("/api")
@Intercept
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Inject
    TokenManager token;

    @GET
    @Path("/allUsers")
    @RolesAllowed({"USER", "GUEST"})
    public Response fetchAllUsers(){
        return Response.ok(User.listAll()).build();
    }

    @POST
    @Path("/addUser")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response addUser(final UserDTO dto){
        return Response.ok(User.add(dto)).build();
    }

    @POST
    @Path("/signIn")
    public Response signIn(final UserDTO userDTO, @Context HttpServerRequest httpServerRequest){
        var user = User.findByUsername(userDTO.getUsername());
        return user != null && verifyPassword(user.password, userDTO.getPassword())
                ? token.createSuccessLoginResponse(user, httpServerRequest) : token.createFailedLoginResponse();
    }
}
