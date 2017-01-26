package ua.training.model.entities;

import ua.training.model.entities.person.User;

import java.time.LocalDateTime;

public class Application {
    private int id;
    private TypeOfWork typeOfWork;
    private User tenant;
    private ProblemScale scaleOfProblem;
    private LocalDateTime desiredTime;

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

    public ProblemScale getScaleOfProblem() {
        return scaleOfProblem;
    }

    public void setScaleOfProblem(ProblemScale scaleOfProblem) {
        this.scaleOfProblem = scaleOfProblem;
    }

    public LocalDateTime getDesiredTime() {
        return desiredTime;
    }

    public void setDesiredTime(LocalDateTime desiredTime) {
        this.desiredTime = desiredTime;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", typeOfWork='" + typeOfWork + '\'' +
                ", tenant=" + tenant +
                ", scaleOfProblem='" + scaleOfProblem + '\'' +
                ", desiredTime=" + desiredTime +
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

        public Builder setScaleOfProblem(ProblemScale scaleOfProblem) {
            application.setScaleOfProblem(scaleOfProblem);
            return this;
        }

        public Builder setDesiredTime(LocalDateTime desiredTime) {
            application.setDesiredTime(desiredTime);
            return this;
        }

        public Application build() {
            return application;
        }
    }
}
