import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private Date orderDate;
    private String status;
    private double totalAmount;

    private Client client;
    private Seller seller;
    private List<OrderDetail> orderDetails; // Composition: 1 Order contains 1..* OrderDetails
    private Payment payment;                // 0..1 Payment

    public Order(int orderId, Client client, Seller seller) {
        this.orderId = orderId;
        this.orderDate = new Date();
        this.status = "Pending";
        this.totalAmount = 0.0;
        this.client = client;
        this.seller = seller;
        this.orderDetails = new ArrayList<>();
    }

    // Composition: Order creates and owns its OrderDetail items
    public void addOrderDetail(int detailId, Cookie cookie, int quantity) {
        orderDetails.add(new OrderDetail(detailId, cookie, quantity));
    }

    public void setPaymentMethod(int paymentId, String method) {
        this.payment = new Payment(paymentId, this.totalAmount, method, this);
    }

    // Follows Sequence Diagram steps 1 through 1.17
    public void createOrder() {
        if (orderDetails.isEmpty()) {
            System.out.println("Order must contain at least 1 cookie item.");
            cancelOrder();
            return;
        }

        boolean allItemsInStock = true;

        // 1.1 - 1.6: Loop for each cookie item
        for (OrderDetail detail : orderDetails) {
            Cookie cookie = detail.getCookie();
            String details = cookie.getDetails();                                // 1.1 / 1.2
            System.out.println("Checking: " + details);

            boolean isAvailable = cookie.checkStock(detail.getQuantity());       // 1.3 / 1.4
            if (!isAvailable) {
                allItemsInStock = false;
            }
            detail.calcSubtotal(cookie.getPrice(), detail.getQuantity());        // 1.5 / 1.6
        }

        // alt [all items in stock]
        if (allItemsInStock) {
            calculateTotal();                                                    // 1.7
            if (payment == null) {
                payment = new Payment(orderId, totalAmount, "Card", this);
            } else {
                payment = new Payment(payment.getPaymentId(), totalAmount, payment.getMethod(), this);
            }

            boolean paymentSuccess = payment.processPayment();                   // 1.8 / 1.9

            // alt [paymentSuccess == true]
            if (paymentSuccess) {
                for (OrderDetail detail : orderDetails) {
                    detail.getCookie().reduceStock(detail.getQuantity());        // 1.10 / 1.11
                }
                confirmOrder();                                                  // 1.12
                System.out.println("-> Result to Client: Order Confirmed");      // 1.13
            } else {
                cancelOrder();                                                   // 1.14
                System.out.println("-> Result to Client: Payment Failed");       // 1.15
            }
        } else {
            cancelOrder();                                                       // 1.16
            System.out.println("-> Result to Client: Out of Stock");             // 1.17
        }
    }

    public void confirmOrder() {
        this.status = "Confirmed";
        client.addOrder(this);
        seller.addManagedOrder(this);
    }

    public void cancelOrder() {
    this.status = "Cancelled";
    client.addOrder(this);
    seller.addManagedOrder(this);
    }

    public double calculateTotal() {
        double sum = 0.0;
        for (OrderDetail detail : orderDetails) {
            sum += detail.getSubtotal();
        }
        this.totalAmount = sum;
        return this.totalAmount;
    }

    public String getOrderDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(orderId)
          .append(" | Client: ").append(client.getName())
          .append(" | Status: ").append(status)
          .append(" | Total: $").append(String.format("%.2f", totalAmount))
          .append(" | Date: ").append(orderDate)
          .append("\n");
        for (OrderDetail detail : orderDetails) {
            sb.append("   - ").append(detail.getCookie().getFlavor())
              .append(" x").append(detail.getQuantity())
              .append(" ($").append(String.format("%.2f", detail.getSubtotal())).append(")\n");
        }
        return sb.toString();
    }


    public int getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public Payment getPayment() { return payment; }
}