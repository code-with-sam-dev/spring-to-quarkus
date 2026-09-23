package dev.codewithsam.payments;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    private final PaymentService payments;

    public PaymentResource(PaymentService payments) {
        this.payments = payments;
    }

    @POST
    public Response create(NewPayment request) {
        Long id = payments.record(request.amountInMinorUnits(), request.currency());
        return Response.created(URI.create("/payments/" + id)).build();
    }

    @GET
    @Path("/{id}")
    public PaymentView find(@PathParam("id") Long id) {
        return payments.find(id).orElseThrow(NotFoundException::new);
    }

    public record NewPayment(long amountInMinorUnits, String currency) {
    }
}
