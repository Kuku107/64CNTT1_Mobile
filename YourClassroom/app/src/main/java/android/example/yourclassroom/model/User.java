package android.example.yourclassroom.model;

public class User {
    private String email;
    private String password;
    private String fullname;
    private String id;

    public User() {
    }

    public User(String email, String password, String fullname, String id) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
