package ua.training.model.entities.person;

public abstract class User extends Person {
    protected String email;
    protected String password;
    protected Role role;

    public enum Role {TENANT, DISPATCHER}

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

    public Role getRole() {
        return role;
    }
}
