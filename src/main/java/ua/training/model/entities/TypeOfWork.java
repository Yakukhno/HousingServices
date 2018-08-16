package ua.training.model.entities;

import static ua.training.util.RepositoryConstants.TYPE_OF_WORK_ID;
import static ua.training.util.RepositoryConstants.TYPE_OF_WORK_TABLE;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ua.training.util.JpaConstants;

@Entity
@Table(name = TYPE_OF_WORK_TABLE)
@NamedQueries({
        @NamedQuery(name = JpaConstants.TYPE_OF_WORK_FIND_BY_DESCRIPTION,
                query = "select t from TypeOfWork t where description like '%' + :description + '%'"),
        @NamedQuery(name = JpaConstants.TYPE_OF_WORK_FIND_ALL, query = "select t from TypeOfWork t"),
        @NamedQuery(name = JpaConstants.TYPE_OF_WORK_DELETE_BY_ID, query = "delete from TypeOfWork t where t.id = :id")
})
public class TypeOfWork {

    @Id
    @GeneratedValue
    @Column(name = TYPE_OF_WORK_ID)
    private int id;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) { return true; }
        if(o == null || getClass() != o.getClass()) { return false; }
        TypeOfWork that = (TypeOfWork)o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return "TypeOfWork{" + "id=" + id + ", description='" + description + '\'' + '}';
    }

    public static class Builder {
        private TypeOfWork typeOfWork = new TypeOfWork();

        public Builder setId(int id) {
            typeOfWork.setId(id);
            return this;
        }

        public Builder setDescription(String description) {
            typeOfWork.setDescription(description);
            return this;
        }

        public TypeOfWork build() {
            return typeOfWork;
        }
    }
}
