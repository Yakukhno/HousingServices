package ua.training.model.entities;

public class Task {
    private int id;
    private Application application;
    private Brigade brigade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Brigade getBrigade() {
        return brigade;
    }

    public void setBrigade(Brigade brigade) {
        this.brigade = brigade;
    }

    public static class Builder {
        private Task task = new Task();

        public Builder setId(int id) {
            task.id = id;
            return this;
        }

        public Builder setApplication(Application application) {
            task.application = application;
            return this;
        }

        public Builder setBrigade(Brigade brigade) {
            task.brigade = brigade;
            return this;
        }
    }
}
