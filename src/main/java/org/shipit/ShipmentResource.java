package org.shipit;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/shipit")
public class ShipmentResource {

    public static List<Shipment> shipments = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/shipments")
    public Response getShipments() {
        return Response.ok(shipments).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/shipments")
    public Response addNewShipment(Shipment newShipment) {
        shipments.add(newShipment);
        return Response.ok(shipments).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/shipments/count")
    public Integer countShipments() {
        return shipments.size();
    }

    @PUT
    @Path("/shipments/{shipmentId}/{shipmentName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateShipment(
            @PathParam("shipmentId") Integer shipmentId,
            @PathParam("shipmentName") String shipmentName) {
        shipments = shipments.stream().map(shipment -> {
            if(shipment.getShipmentId().equals(shipmentId)) {
                shipment.setShipmentName(shipmentName);
            }
            return shipment;
        }).collect(Collectors.toList());
        return Response.ok(shipments).build();
    }

    @DELETE
    @Path("/shipments/{shipmentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteShipment(
            @PathParam("shipmentId") Integer shipmentId) {
        Optional<Shipment> shipmentToDelete = shipments.stream().filter(shipment -> shipment.getShipmentId().equals(shipmentId))
                .findFirst();
        boolean removed = false;
        if(shipmentToDelete.isPresent()) {
            removed = shipments.remove(shipmentToDelete.get());
        }
        if(removed) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}