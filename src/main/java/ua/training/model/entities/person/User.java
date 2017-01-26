package ua.training.model.entities.person;

public class User extends Person {
    private String email;
    private String password;
    private Role role;

    public enum Role {
        TENANT, DISPATCHER
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public static class Builder  {
        private User user = new User();

        public Builder setId(int id) {
            user.setId(id);
            return this;
        }

        public Builder setName(String name) {
            user.setName(name);
            return this;
        }

        public Builder setEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder setPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder setRole(Role role) {
            user.setRole(role);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
