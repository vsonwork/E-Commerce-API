package com.ecommerce.resource;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
public class CartResource {

    @POST
    public Response addToCart(@QueryParam("productId") Long productId, @QueryParam("quantity") int quantity, @Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        User user = User.find("username", username).firstResult();
        Product product = Product.findById(productId);
        if (product == null || product.stock < quantity) {
            return Response.status(400).entity("Not enough stock").build();
        }

        CartItem item = new CartItem();
        item.user = user;
        item.product = product;
        item.quantity = quantity;
        item.persist();

        return Response.ok().build();
    }

    @GET
    public List<CartItem> getCart(@Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        return CartItem.list("user.username", username);
    }
}