package ua.training.model.entities;

import java.util.List;

public class Brigade {
    private int id;
    private List<Worker> workers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }

    public static class Builder {
        private Brigade brigade = new Brigade();

        public Builder setId(int id) {
            brigade.id = id;
            return this;
        }

        public Builder setWorkers(List<Worker> workers) {
            brigade.workers = workers;
            return this;
        }

        public Builder addWorker(Worker worker) {
            brigade.workers.add(worker);
            return this;
        }
    }
}
