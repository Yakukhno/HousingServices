package ua.training.model.entities;

public class Tenant {
    private int id;
    private int account;
    private String name;
    private String email;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static class Builder {
        private Tenant tenant = new Tenant();

        public Builder setId(int id) {
            tenant.id = id;
            return this;
        }

        public Builder setAccount(int account) {
            tenant.account = account;
            return this;
        }

        public Builder setName(String name) {
            tenant.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            tenant.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            tenant.password = password;
            return this;
        }

        public Tenant build() {
            return tenant;
        }
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", account=" + account +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
