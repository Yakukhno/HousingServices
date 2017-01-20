package ua.training.model.entities.person;

public class Dispatcher extends User {

    {
        role = Role.DISPATCHER;
    }

    private boolean isOnline;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return "Dispatcher{" +
                "id=" + id +
                ", isOnline=" + isOnline +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static class Builder  {
        private Dispatcher dispatcher = new Dispatcher();

        public Builder setId(int id) {
            dispatcher.setId(id);
            return this;
        }

        public Builder setName(String name) {
            dispatcher.setName(name);
            return this;
        }

        public Builder setOnline(boolean isOnline) {
            dispatcher.setOnline(isOnline);
            return this;
        }

        public Builder setEmail(String email) {
            dispatcher.setEmail(email);
            return this;
        }

        public Builder setPassword(String password) {
            dispatcher.setPassword(password);
            return this;
        }

        public Dispatcher build() {
            return dispatcher;
        }
    }
}
