package ua.training.model.entities;

import java.util.HashSet;
import java.util.Set;

import ua.training.model.entities.person.Worker;

public class Brigade {
    private int id;
    private Worker manager;
    private Set<Worker> workers = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Worker getManager() {
        return manager;
    }

    public void setManager(Worker manager) {
        this.manager = manager;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    @Override
    public String toString() {
        return "Brigade{" +
                "id=" + id +
                ", manager=" + manager +
                ", workers=" + workers +
                '}';
    }

    public static class Builder {
        private Brigade brigade = new Brigade();

        public Builder setId(int id) {
            brigade.setId(id);
            return this;
        }

        public Builder setManager(Worker manager) {
            brigade.setManager(manager);
            return this;
        }

        public Builder setWorkers(Set<Worker> workers) {
            brigade.setWorkers(workers);
            return this;
        }

        public Builder addWorker(Worker worker) {
            brigade.addWorker(worker);
            return this;
        }

        public Brigade build() {
            return brigade;
        }
    }
}
