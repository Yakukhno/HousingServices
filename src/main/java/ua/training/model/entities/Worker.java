package ua.training.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private int id;
    private List<TypeOfWork> typesOfWork = new ArrayList<>();
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TypeOfWork> getTypesOfWork() {
        return typesOfWork;
    }

    public void setTypesOfWork(List<TypeOfWork> typesOfWork) {
        this.typesOfWork = typesOfWork;
    }

    public void addTypeOfWork(TypeOfWork typeOfWork) {
        typesOfWork.add(typeOfWork);
    }

    public void removeTypeOfWork(TypeOfWork typeOfWork) {
        typesOfWork.remove(typeOfWork);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", typesOfWork=" + typesOfWork +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private Worker worker = new Worker();

        public Builder setId(int id) {
            worker.id = id;
            return this;
        }

        public Builder addTypeOfWork(TypeOfWork typeOfWork) {
            worker.addTypeOfWork(typeOfWork);
            return this;
        }

        public Builder setTypesOfWork(List<TypeOfWork> typesOfWork) {
            worker.typesOfWork = typesOfWork;
            return this;
        }

        public Builder setName(String name) {
            worker.name = name;
            return this;
        }

        public Worker build() {
            return worker;
        }
    }
}
