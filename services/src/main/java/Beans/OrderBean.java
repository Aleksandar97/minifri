package Beans;
import Entities.Customer;
import Entities.Order;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.Date;

@ApplicationScoped
public class OrderBean {

    @PersistenceContext(unitName = "kpdb-jpa")
    private EntityManager em;

    private Logger log = Logger.getLogger(CustomerBean.class.getName());

    @Inject
    CustomerBean customerBean;

    @PostConstruct
    private void init() {
        log.info(OrderBean.class.getName() + " initializing.");
    }

    @Default
    public List<Order> getOrders(QueryParameters query, Integer customerId) {

        return customerBean.getCustomer(customerId).getOrders();

    }

    @Default
    public Order getOrder(Integer orderId) {

        Order order = em.find(Order.class, orderId);

        if (order == null) {
            throw new NotFoundException();
        }

        return order;
    }


    @Transactional
    public Order createOrderWithCID(Order order, int CId) {

        try {
            beginTx();
            order.setDate(new Date());
            em.persist(order);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        customerBean.getCustomer(CId).getOrders().add(order);
        return order;
    }

    @Transactional
    public Order putOrder(int orderId, Order order) {

        Order o = em.find(Order.class, orderId);

        if (o == null) {
            return null;
        }

        try {
            beginTx();
            order.setId(o.getId());
            order.setDate(new Date());
            order = em.merge(order);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return order;
    }

    @Transactional
    public boolean deleteOrder(Integer customerId, Integer orderId) {

        Order o = em.find(Order.class, orderId);

        if (o != null) {
            customerBean.getCustomer(customerId).getOrders().remove(o);
            try {
                beginTx();
                em.remove(o);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
