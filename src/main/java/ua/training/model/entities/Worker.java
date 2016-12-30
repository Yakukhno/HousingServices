package ua.training.model.entities;

public class Worker {
    private int id;
    private String typeOfWork;
    private String name;

    public int getId() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {
        private Worker worker = new Worker();

        public Builder setId(int id) {
            worker.id = id;
            return this;
        }

        public Builder setTypeOfWork(String typeOfWork) {
            worker.typeOfWork = typeOfWork;
            return this;
        }

        public Builder setName(String name) {
            worker.name = name;
            return this;
        }
    }
}
