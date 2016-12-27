package ua.training.model.entities;

import java.sql.Date;
import java.sql.Timestamp;

public class Application {
    private int id;
    private TypeOfWork typeOfWork;
    private Tenant tenant;
    private String scaleOfProblem;
    private Timestamp desiredTime;

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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getScaleOfProblem() {
        return scaleOfProblem;
    }

    public void setScaleOfProblem(String scaleOfProblem) {
        this.scaleOfProblem = scaleOfProblem;
    }

    public Timestamp getDesiredTime() {
        return desiredTime;
    }

    public void setDesiredTime(Timestamp desiredTime) {
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
}
