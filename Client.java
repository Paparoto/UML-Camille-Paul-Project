import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private String shippingAddress;
    private List<Order> orders; // 1 Client places 0..* Orders

    public Client(int userId, String name, String email, String password, String shippingAddress) {
        super(userId, name, email, password);
        this.shippingAddress = shippingAddress;
        this.orders = new ArrayList<>();
    }

    public void register() {
        System.out.println("Client registered successfully: " + getName() + " (" + getEmail() + ")");
    }

    public String trackOrder(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return "Order #" + orderId + " status: " + order.getStatus();
            }
        }
        return "Order #" + orderId + " not found.";
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public String getShippingAddress() { return shippingAddress; }
    public List<Order> getOrders() { return orders; }
}