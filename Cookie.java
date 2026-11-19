public class Cookie {
    private int cookieId;
    private String flavor;
    private double price;
    private int stockAvailable;

    public Cookie(int cookieId, String flavor, double price, int stockAvailable) {
        this.cookieId = cookieId;
        this.flavor = flavor;
        this.price = price;
        this.stockAvailable = stockAvailable;
    }

    public String getDetails() {
        return "Cookie #" + cookieId + " [" + flavor + "] - $" +
               String.format("%.2f", price) + " (Stock: " + stockAvailable + ")";
    }

    public boolean checkStock(int amount) {
        return amount > 0 && stockAvailable >= amount;
    }

    public boolean reduceStock(int amount) {
        if (checkStock(amount)) {
            stockAvailable -= amount;
            return true;
        }
        return false;
    }

    public void addStock(int amount) {
        if (stockAvailable + amount >= 0) {
            stockAvailable += amount;
        }
    }

    public int getCookieId() { return cookieId; }
    public String getFlavor() { return flavor; }
    public double getPrice() { return price; }
    public int getStockAvailable() { return stockAvailable; }
}