package ua.training.model.entities.person;

import static ua.training.util.RepositoryConstants.TYPE_OF_WORK_ID;
import static ua.training.util.RepositoryConstants.WORKER_HAS_TYPE_OF_WORK_TABLE;
import static ua.training.util.RepositoryConstants.WORKER_ID;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import ua.training.model.entities.TypeOfWork;
import ua.training.util.JpaConstants;

@Entity
@AttributeOverride(name = "id", column = @Column(name = WORKER_ID))
@NamedQueries({
        @NamedQuery(name = JpaConstants.WORKER_FIND_BY_TYPE_OF_WORK,
                query = "select w from Worker w, in (w.typesOfWork) t where t.id = :id"),
        @NamedQuery(name = JpaConstants.WORKER_FIND_ALL, query = "select w from Worker w"),
        @NamedQuery(name = JpaConstants.WORKER_DELETE_BY_ID, query = "delete from Worker w where w.id = :id")
})
public class Worker extends Person {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = WORKER_HAS_TYPE_OF_WORK_TABLE,
            joinColumns = @JoinColumn(name = WORKER_ID),
            inverseJoinColumns = @JoinColumn(name = TYPE_OF_WORK_ID))
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
        if (this == o) { return true; }
        if (!(o instanceof Worker)) { return false; }
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
