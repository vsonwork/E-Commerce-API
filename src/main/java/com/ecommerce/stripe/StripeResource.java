package com.ecommerce.stripe;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.Map;

@Path("/checkout")
@Produces(MediaType.APPLICATION_JSON)
public class StripeResource {

    @POST
    public Response checkout(@Context SecurityContext ctx) throws StripeException {
        String username = ctx.getUserPrincipal().getName();
        User user = User.find("username", username).firstResult();
        List<CartItem> items = CartItem.list("user", user);

        long totalAmount = items.stream()
                .mapToLong(i -> (long)(i.product.price * 100 * i.quantity)) // Stripe uses cents
                .sum();

        Stripe.apiKey = "sk_test_xxx";

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(totalAmount)
                        .setCurrency("usd")
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return Response.ok(Map.of("clientSecret", intent.getClientSecret())).build();
    }
}
