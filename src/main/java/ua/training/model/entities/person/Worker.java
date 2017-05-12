package ua.training.model.entities.person;

import ua.training.model.entities.TypeOfWork;

import java.util.*;

public class Worker extends Person {

    private Set<TypeOfWork> typesOfWork = new HashSet<>();

    public Set<TypeOfWork> getTypesOfWork() {
        return typesOfWork;
    }

    public void addTypeOfWork(TypeOfWork typeOfWork) {
        typesOfWork.add(typeOfWork);
    }

    public void setTypesOfWork(Set<TypeOfWork> typesOfWork) {
        this.typesOfWork = typesOfWork;
    }

    public void removeTypeOfWork(TypeOfWork typeOfWork) {
        typesOfWork.remove(typeOfWork);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;
        Worker worker = (Worker) o;
        return Objects.equals(name, worker.name) &&
                Objects.equals(typesOfWork, worker.typesOfWork);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typesOfWork);
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

        public Builder setName(String name) {
            worker.setName(name);
            return this;
        }

        public Worker build() {
            return worker;
        }
    }
}
