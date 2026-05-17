package university.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Abstract base class for all users in the University System.
 * <p>
 * Every participant in the system (students, teachers, managers, admins)
 * extends this class. Passwords are stored as SHA-256 hashes for security.
 * </p>
 *
 * @author Kozhabergenov Nurzhan, Yelmuratova Madina, Kainazar Nurdaulet
 * @version 1.0
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the user. */
    private String userId;

    /** Login name used for authentication. */
    private String login;

    /** SHA-256 hashed password. */
    private String password;

    /** Email address of the user. */
    private String email;

    /**
     * Constructs a new User with the given credentials.
     * The password is automatically hashed using SHA-256.
     *
     * @param userId   unique identifier for this user
     * @param login    login name for authentication
     * @param password plain-text password (will be hashed)
     * @param email    email address of the user
     */
    public User(String userId, String login, String password, String email) {
        this.userId = userId;
        this.login = login;
        this.password = hash(password);
        this.email = email;
    }

    /**
     * Hashes a plain-text password using SHA-256 algorithm.
     *
     * @param password the plain-text password to hash
     * @return hexadecimal string representation of the SHA-256 hash
     * @throws RuntimeException if SHA-256 algorithm is not available
     */
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

    /**
     * Authenticates the user by comparing the given password with the stored hash.
     *
     * @param inputPassword the plain-text password to verify
     * @return {@code true} if the password matches, {@code false} otherwise
     */
    public boolean authenticate(String inputPassword) {
        return this.password.equals(hash(inputPassword));
    }

    /**
     * Returns a string representation of the user's information.
     * Must be implemented by all subclasses.
     *
     * @return formatted string with user details
     */
    public abstract String getUserInfo();

    /**
     * Returns the unique identifier of this user.
     *
     * @return the user ID
     */
    public String getUserId() { return userId; }

    /**
     * Returns the login name of this user.
     *
     * @return the login name
     */
    public String getLogin() { return login; }

    /**
     * Returns the email address of this user.
     *
     * @return the email address
     */
    public String getEmail() { return email; }

    /**
     * Updates the email address of this user.
     *
     * @param email the new email address
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Updates the password of this user.
     * The new password is automatically hashed using SHA-256.
     *
     * @param p the new plain-text password
     */
    public void setPassword(String p) { this.password = hash(p); }

    /**
     * Returns the hashed password. Access is restricted to subclasses.
     *
     * @return the SHA-256 hashed password
     */
    protected String getPassword() { return password; }

    /**
     * Checks equality based on user ID.
     *
     * @param o the object to compare with
     * @return {@code true} if both users have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    /**
     * Returns a hash code based on the user ID.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    /**
     * Returns a string representation of this user.
     *
     * @return string containing class name, ID, login, and email
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + userId + "', login='" + login + "', email='" + email + "'}";
    }
}
