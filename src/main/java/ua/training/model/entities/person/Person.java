package ua.training.model.entities.person;

import static ua.training.util.RepositoryConstants.USER_ID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@AttributeOverride(name = "id", column = @Column(name = USER_ID))
public abstract class Person {

    @Id
    @GeneratedValue
    protected int id;
    protected String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
