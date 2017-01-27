package ua.training.model.entities.person;

import ua.training.model.entities.TypeOfWork;

import java.util.ArrayList;
import java.util.List;

public class Worker extends Person {

    private List<TypeOfWork> typesOfWork = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;

        Worker worker = (Worker) o;

        if (id != worker.id) return false;
        return name.equals(worker.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typesOfWork=" + typesOfWork +
                '}';
    }

    public static class Builder {
        private Worker worker = new Worker();

        public Builder setId(int id) {
            worker.setId(id);
            return this;
        }

        public Builder addTypeOfWork(TypeOfWork typeOfWork) {
            worker.addTypeOfWork(typeOfWork);
            return this;
        }

        public Builder setTypesOfWork(List<TypeOfWork> typesOfWork) {
            worker.setTypesOfWork(typesOfWork);
            return this;
        }

        public Builder setName(String name) {
            worker.setName(name);
            return this;
        }

        public Worker build() {
            return worker;
        }
    }
}
