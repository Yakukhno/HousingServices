package ua.training.model.entities.person;

public class Tenant extends User {
    private int account;

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
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

    public static class Builder {
        private Tenant tenant = new Tenant();

        public Builder setId(int id) {
            tenant.setId(id);
            return this;
        }

        public Builder setAccount(int account) {
            tenant.setAccount(account);
            return this;
        }

        public Builder setName(String name) {
            tenant.setName(name);
            return this;
        }

        public Builder setEmail(String email) {
            tenant.setEmail(email);
            return this;
        }

        public Builder setPassword(String password) {
            tenant.setPassword(password);
            return this;
        }

        public Tenant build() {
            return tenant;
        }
    }
}
