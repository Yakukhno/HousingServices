package ua.training.model.entities;

public class Application {
    private int id;
    private String typeOfWork;
    private Tenant tenant;
    private String scaleOfProblem;
    private String desiredTime;

    public int getId()  {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
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

    public String getDesiredTime() {
        return desiredTime;
    }

    public void setDesiredTime(String desiredTime) {
        this.desiredTime = desiredTime;
    }
}
