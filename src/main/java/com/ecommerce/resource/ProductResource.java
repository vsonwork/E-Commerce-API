package com.ecommerce.resource;

import com.ecommerce.model.Product;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    @GET
    public List<Product> listAll() {
        return Product.listAll();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response add(Product product) {
        product.persist();
        return Response.status(201).entity(product).build();
    }
}