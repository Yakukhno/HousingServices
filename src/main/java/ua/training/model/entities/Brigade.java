package ua.training.model.entities;

import static ua.training.util.RepositoryConstants.BRIGADE_HAS_WORKER_TABLE;
import static ua.training.util.RepositoryConstants.BRIGADE_ID;
import static ua.training.util.RepositoryConstants.BRIGADE_MANAGER;
import static ua.training.util.RepositoryConstants.WORKER_ID;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import ua.training.model.entities.person.Worker;

@Entity
public class Brigade {

    @Id
    @GeneratedValue
    @Column(name = BRIGADE_ID)
    private int id;

    @ManyToOne
    @JoinColumn(name = BRIGADE_MANAGER)
    private Worker manager;

    @ManyToMany
    @JoinTable(name = BRIGADE_HAS_WORKER_TABLE,
            joinColumns = @JoinColumn(name = BRIGADE_ID),
            inverseJoinColumns = @JoinColumn(name = WORKER_ID))
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
