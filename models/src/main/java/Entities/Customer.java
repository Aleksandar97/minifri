package Entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NamedQueries(value =
        {
                @NamedQuery(name = "Customer.getAll", query = "SELECT c FROM customer c")
        })
@XmlRootElement(name = "customer")
@Entity(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String address;

    @OneToMany(fetch=FetchType.EAGER)
    private List<Order> orders;

    public Customer() {}

    public Customer (int id, String name, String last, String add){
        this.id = id;
        this.firstName = name;
        this.lastName = last;
        this.address = add;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order>  getOrders() {
        return this.orders;
    }

    public void setOrders(List<Order>  orders) {
        this.orders = orders;
    }

    public void putOrders(Order order) {
        this.orders.add(order);
    }

    public void removeOrders(Order order) {
        this.orders.remove(order);
    }
}