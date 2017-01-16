package ua.training.model.entities;

public class Task {
    private int id;
    private Application application;
    private Brigade brigade;
    private boolean isActive;

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setBrigade(Brigade brigade) {
        this.brigade = brigade;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", application=" + application +
                ", brigade=" + brigade +
                ", isActive=" + isActive +
                '}';
    }

    public static class Builder {
        private Task task = new Task();

        public Builder setId(int id) {
            task.setId(id);
            return this;
        }

        public Builder setApplication(Application application) {
            task.setApplication(application);
            return this;
        }

        public Builder setBrigade(Brigade brigade) {
            task.setBrigade(brigade);
            return this;
        }

        public Builder setActive(boolean isActive) {
            task.setActive(isActive);
            return this;
        }

        public Task build() {
            return task;
        }
    }
}
