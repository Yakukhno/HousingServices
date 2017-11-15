package ua.training.model.entities;

import java.time.LocalDateTime;

import ua.training.model.entities.person.User;

public class Application {
    private int id;
    private TypeOfWork typeOfWork;
    private User user;
    private ProblemScale problemScale;
    private LocalDateTime desiredTime;
    private Status status;
    private String address;

    public enum Status {
        NEW, CONSIDERED
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(TypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProblemScale getProblemScale() {
        return problemScale;
    }

    public void setProblemScale(ProblemScale problemScale) {
        this.problemScale = problemScale;
    }

    public LocalDateTime getDesiredTime() {
        return desiredTime;
    }

    public void setDesiredTime(LocalDateTime desiredTime) {
        this.desiredTime = desiredTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", typeOfWork=" + typeOfWork +
                ", user=" + user +
                ", problemScale=" + problemScale +
                ", desiredTime=" + desiredTime +
                ", status=" + status +
                ", address='" + address + '\'' +
                '}';
    }

    public static class Builder {
        private Application application = new Application();

        public Builder setId(int id) {
            application.setId(id);
            return this;
        }

        public Builder setTypeOfWork(TypeOfWork typeOfWork) {
            application.setTypeOfWork(typeOfWork);
            return this;
        }

        public Builder setUser(User user) {
            application.setUser(user);
            return this;
        }

        public Builder setProblemScale(ProblemScale problemScale) {
            application.setProblemScale(problemScale);
            return this;
        }

        public Builder setDesiredTime(LocalDateTime desiredTime) {
            application.setDesiredTime(desiredTime);
            return this;
        }

        public Builder setStatus(Status status) {
            application.setStatus(status);
            return this;
        }

        public Builder setAddress(String address) {
            application.setAddress(address);
            return this;
        }

        public Application build() {
            return application;
        }
    }
}
