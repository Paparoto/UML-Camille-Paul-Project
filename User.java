public abstract class User {
    private int userId;
    private String name;
    private String email;
    private String password;

    public User(int userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email.equalsIgnoreCase(email) && this.password.equals(password);
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}