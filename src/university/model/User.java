package university.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Objects;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String login;
    private String password;
    private String email;

    public User(String userId, String login, String password, String email) {
        this.userId = userId;
        this.login = login;
        this.password = hash(password);
        this.email = email;
    }

    private static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(hash(inputPassword));
    }

    public abstract String getUserInfo();

    // Getters & Setters
    public String getUserId()              { return userId; }
    public String getLogin()               { return login; }
    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }
    public void setPassword(String p)      { this.password = hash(p); }
    protected String getPassword()         { return password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + userId + "', login='" + login + "', email='" + email + "'}";
    }
}
