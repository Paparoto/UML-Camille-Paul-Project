import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<Order> managedOrders; // 1 Seller manages 0..* Orders
    private List<Cookie> cookies;      // 1 Seller updates 0..* Cookies

    public Seller(int userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.managedOrders = new ArrayList<>();
        this.cookies = new ArrayList<>();
    }

    public String viewOrders() {
        if (managedOrders.isEmpty()) {
            return "No orders found.";
        }
        StringBuilder sb = new StringBuilder("=== Managed Orders ===\n");
        for (Order order : managedOrders) {
            sb.append(order.getOrderDetails()).append("\n");
        }
        return sb.toString();
    }

    public void fulfillOrder(int orderId) {
        for (Order order : managedOrders) {
            if (order.getOrderId() == orderId) {
                if ("Confirmed".equalsIgnoreCase(order.getStatus())) {
                    // <<Include>> Update Order Status
                    updateOrderStatus(orderId, "Fulfilled");
                    System.out.println("Order #" + orderId + " has been fulfilled.");
                } else {
                    System.out.println("Cannot fulfill Order #" + orderId + " (current status: " + order.getStatus() + ").");
                }
                return;
            }
        }
        System.out.println("Order #" + orderId + " not found.");
    }

    public void updateInventory(int cookieId, int amount) {
        for (Cookie cookie : cookies) {
            if (cookie.getCookieId() == cookieId) {
                cookie.addStock(amount);
                System.out.println("Inventory updated for " + cookie.getFlavor() +
                                   ". New stock: " + cookie.getStockAvailable());
                return;
            }
        }
        System.out.println("Cookie #" + cookieId + " not found.");
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        for (Order order : managedOrders) {
            if (order.getOrderId() == orderId) {
                order.setStatus(newStatus);
                System.out.println("Order #" + orderId + " status updated to: " + newStatus);
                return;
            }
        }
        System.out.println("Order #" + orderId + " not found.");
    }

    public void addManagedOrder(Order order) { managedOrders.add(order); }
    public void addCookie(Cookie cookie) { cookies.add(cookie); }
    public List<Cookie> getCookies() { return cookies; }
}