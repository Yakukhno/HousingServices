package ua.training.model.entities;

import ua.training.model.entities.person.User;

import java.time.LocalDateTime;

public class Application {
    private int id;
    private TypeOfWork typeOfWork;
    private User tenant;
    private ProblemScale problemScale;
    private LocalDateTime desiredTime;
    private Status status;
    private String street;
    private String houseNumber;
    private String flatNumber;

    public enum Status {
        NEW, CONSIDERED
    }

    public int getId()  {
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

    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", typeOfWork=" + typeOfWork +
                ", tenant=" + tenant +
                ", problemScale=" + problemScale +
                ", desiredTime=" + desiredTime +
                ", status=" + status +
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

        public Builder setTenant(User tenant) {
            application.setTenant(tenant);
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

        public Builder setStreet(String street) {
            application.setStreet(street);
            return this;
        }

        public Builder setHouseNumber(String houseNumber) {
            application.setHouseNumber(houseNumber);
            return this;
        }

        public Builder setFlatNumber(String flatNumber) {
            application.setFlatNumber(flatNumber);
            return this;
        }

        public Application build() {
            return application;
        }
    }
}
