package com.ecommerce.auth;

import com.ecommerce.model.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @POST
    @Path("/signup")
    public Response signup(AuthRequest request) {
        if (User.find("username", request.username).firstResult() != null) {
            return Response.status(409).build();
        }
        User user = new User();
        user.username = request.username;
        user.password = BCrypt.hashpw(request.password, BCrypt.gensalt());
        user.role = "USER";
        user.persist();
        return Response.ok().build();
    }

    @POST
    @Path("/login")
    public Response login(AuthRequest request) {
        User user = User.find("username", request.username).firstResult();
        if (user == null || !BCrypt.checkpw(request.password, user.password)) {
            return Response.status(401).build();
        }

        String token = TokenUtils.generateToken(user.username, user.role);
        return Response.ok().entity(Map.of("token", token)).build();
    }
}