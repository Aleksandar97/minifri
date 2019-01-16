package v1.resources;

import Beans.CustomerBean;
import Beans.OrderBean;
import Entities.Order;
import com.kumuluz.ee.rest.beans.QueryParameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("customers/{customerId}/order")
@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class OrderResources {

    @Inject
    private OrderBean orderBean;

    @Context
    protected UriInfo uriInfo;

    private Logger log = Logger.getLogger(CustomerBean.class.getName());

    @GET
    public Response getOrders(@PathParam("customerId") Integer customerId) {

        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<Order> order = orderBean.getOrders(query, customerId);
        //GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(order) {};
        return Response.status(Response.Status.OK).entity(order).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrder(@PathParam("orderId") Integer orderId) {

        Order order = orderBean.getOrder(orderId);

        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(order).build();
    }

    @POST
    public Response createOrder(Order order, @PathParam("customerId") Integer customerId) {

        if ((order.getItem() == null || order.getItem().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            order = orderBean.createOrderWithCID(order, customerId);
        }

        if (order.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(order).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(order).build();
        }
    }

    @PUT
    @Path("/{orderId}")
    public Response putCustomerOrder(@PathParam("orderId") Integer orderId, Order order) {

        order = orderBean.putOrder(orderId, order);

        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (order.getId() != null) {
                return Response.status(Response.Status.OK).entity(order).build();
            } else {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
        }
    }

    @DELETE
    @Path("/{orderId}")
    public Response deleteOrder(@PathParam("customerId") Integer customerId, @PathParam("orderId") Integer orderId) {

        boolean deleted = orderBean.deleteOrder(customerId, orderId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
