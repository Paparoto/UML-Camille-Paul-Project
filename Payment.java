public class Payment {
    private int paymentId;
    private double amount;
    private String method;
    private String status;
    private Order order; // Payment 0..1 processes 1 Order

    public Payment(int paymentId, double amount, String method, Order order) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.method = method;
        this.status = "Pending";
        this.order = order;
    }

    public boolean processPayment() {
        // Simulates payment failure if amount <= 0 or method is "INVALID"
        if (amount > 0 && method != null && !method.equalsIgnoreCase("INVALID")) {
            this.status = "Completed";
            return true;
        }
        this.status = "Failed";
        return false;
    }

    public void refundPayment() {
        if ("Completed".equalsIgnoreCase(this.status)) {
            this.status = "Refunded";
            System.out.println("Payment #" + paymentId + " of $" + String.format("%.2f", amount) + " refunded.");
        } else {
            System.out.println("Cannot refund Payment #" + paymentId + " (status: " + status + ").");
        }
    }

    public int getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public Order getOrder() { return order; }
}