import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Client> clients = new ArrayList<>();
    private static Seller seller;
    private static int nextOrderId = 100;
    private static int nextUserId = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        seller = new Seller(1, "Bakery Admin", "seller@cookies.com", "admin123");
        seller.addCookie(new Cookie(1, "Chocolate Chip", 2.50, 20));
        seller.addCookie(new Cookie(2, "Double Fudge", 3.00, 10));
        seller.addCookie(new Cookie(3, "Oatmeal Raisin", 2.20, 15));

        Client c1 = new Client(2, "Camille", "camille@test.com", "1234", "12 Baker Street");
        Client c2 = new Client(3, "Paul", "paul@test.com", "1122", "9 Cookie Street");
        clients.add(c1);
        clients.add(c2);

        while (true) {
            System.out.println("\n=== COOKIE SELLING SYSTEM ===");
            System.out.println("1. Client Mode (camille@test.com / 1234) (paul@test.com / 1122)");
            System.out.println("2. Register New Client");
            System.out.println("3. Seller Mode (seller@cookies.com / admin123)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                clientLoginAndMenu(scanner);
            } else if (choice == 2) {
                registerClient(scanner);
            } else if (choice == 3) {
                sellerLoginAndMenu(scanner);
            } else if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            }
        }
        scanner.close();
    }

    private static void registerClient(Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        System.out.print("Shipping Address: ");
        String addr = scanner.nextLine();

        Client newClient = new Client(++nextUserId, name, email, pass, addr);
        newClient.register();
        clients.add(newClient);
    }

    private static void clientLoginAndMenu(Scanner scanner) {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        Client loggedIn = null;
        for (Client c : clients) {
            if (c.login(email, pass)) {
                loggedIn = c;
                break;
            }
        }
        if (loggedIn == null) {
            System.out.println("Invalid credentials.");
            return;
        }

        while (true) {
            System.out.println("\n--- Client Menu (" + loggedIn.getName() + ") ---");
            System.out.println("1. Browse Cookies");
            System.out.println("2. Order Cookies");
            System.out.println("3. View Order Status");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            int ch = Integer.parseInt(scanner.nextLine());

            if (ch == 1) {
                printStock();
            } else if (ch == 2) {
                printStock();
                Order order = new Order(++nextOrderId, loggedIn, seller);
                int detailId = 1;
                boolean allInStock = true;
                double totalToPay = 0.0;

                while (true) {
                    System.out.print("Enter Cookie ID to add (0 to checkout): ");
                    int cid = Integer.parseInt(scanner.nextLine());
                    if (cid == 0) break;

                    Cookie selected = null;
                    for (Cookie c : seller.getCookies()) {
                        if (c.getCookieId() == cid) selected = c;
                    }
                    if (selected == null) {
                        System.out.println("Cookie not found.");
                        continue;
                    }
                    System.out.print("Quantity: ");
                    int qty = Integer.parseInt(scanner.nextLine());
                    if (qty <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                        continue;
                    }
                    if (!selected.checkStock(qty)) {
                        allInStock = false;
                    }
                    order.addOrderDetail(detailId++, selected, qty);
                    totalToPay += selected.getPrice() * qty;
                }

                if (detailId == 1) {
                    System.out.println("Cart is empty. Order not created.");
                    nextOrderId--;
                } else {
                    if (allInStock) {
                        System.out.println("Total amount to pay: $" + String.format("%.2f", totalToPay));
                        System.out.print("Payment method (Card / PayPal / INVALID to test failure): ");
                        String method = scanner.nextLine();
                        order.setPaymentMethod(order.getOrderId(), method);
                    }
                    order.createOrder();
                    printStock();
                    System.out.println("Order #" + order.getOrderId() + " final status: " + order.getStatus());
                }
            }else if (ch == 3) {
                System.out.print("Enter Order ID: ");
                int oid = Integer.parseInt(scanner.nextLine());
                System.out.println(loggedIn.trackOrder(oid));
            } else if (ch == 0) {
                break;
            }
        }
    }

    private static void sellerLoginAndMenu(Scanner scanner) {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        if (!seller.login(email, pass)) {
            System.out.println("Invalid credentials.");
            return;
        }

        while (true) {
            printStock();
            System.out.println("\n--- Seller Menu ---");
            System.out.println("1. Update Inventory");
            System.out.println("2. View All Orders");
            System.out.println("3. Fulfill Cookie Order");
            System.out.println("4. Update Order Status Manually");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            int ch = Integer.parseInt(scanner.nextLine());

            if (ch == 1) {
                for (Cookie c : seller.getCookies()) System.out.println(c.getDetails());
                System.out.print("Cookie ID: ");
                int cid = Integer.parseInt(scanner.nextLine());
                System.out.print("Amount to add (or negative to reduce): ");
                int amt = Integer.parseInt(scanner.nextLine());
                seller.updateInventory(cid, amt);
            } else if (ch == 2) {
                System.out.println(seller.viewOrders());
            } else if (ch == 3) {
                System.out.print("Order ID to fulfill: ");
                int oid = Integer.parseInt(scanner.nextLine());
                seller.fulfillOrder(oid);
            } else if (ch == 4) {
                System.out.print("Order ID: ");
                int oid = Integer.parseInt(scanner.nextLine());
                System.out.print("New Status: ");
                String st = scanner.nextLine();
                seller.updateOrderStatus(oid, st);
            } else if (ch == 0) {
                break;
            }
        }
    }
    private static void printStock() {
    System.out.println("\n--- Current Cookie Stock ---");
    for (Cookie cookie : seller.getCookies()) {
        System.out.println(cookie.getDetails());
    }
}
}