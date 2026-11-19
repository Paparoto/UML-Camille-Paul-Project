public class OrderDetail {
    private int detailId;
    private int quantity;
    private double subtotal;
    private Cookie cookie; // OrderDetail 0..* refers to 1 Cookie

    public OrderDetail(int detailId, Cookie cookie, int quantity) {
        this.detailId = detailId;
        this.cookie = cookie;
        this.quantity = quantity;
        this.subtotal = 0.0;
    }

    public double calcSubtotal(double price, int quantity) {
        this.subtotal = price * quantity;
        return this.subtotal;
    }

    public int getDetailId() { return detailId; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return subtotal; }
    public Cookie getCookie() { return cookie; }
}