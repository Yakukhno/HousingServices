package ua.training.model.entities;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private Application application;
    private Brigade brigade;
    private LocalDateTime scheduledTime;
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

    public void setBrigade(Brigade brigade) {
        this.brigade = brigade;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", application=" + application +
                ", brigade=" + brigade +
                ", scheduledTime=" + scheduledTime +
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

        public Builder setScheduledTime(LocalDateTime scheduledTime) {
            task.setScheduledTime(scheduledTime);
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
