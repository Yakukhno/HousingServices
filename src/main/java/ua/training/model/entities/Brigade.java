package ua.training.model.entities;

import ua.training.model.entities.person.Worker;

import java.util.ArrayList;
import java.util.List;

public class Brigade {
    private int id;
    private Worker manager;
    private List<Worker> workers = new ArrayList<>();

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

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
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

        public Builder setWorkers(List<Worker> workers) {
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
