package core;

public class User {
    private int UUID;
    private String username;
    private String password;

    public User(int UUID, String username, String password) {

        this.UUID = UUID;
        this.username = username;
        setPassword(password);
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUUID() {
        return UUID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws IllegalArgumentException {
        try {
            if (password.length() >= 8) {
                this.password = password;
            }
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }
}
