package ua.training.model.entities.person;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import ua.training.util.JpaConstants;

@Entity
@NamedQueries({
        @NamedQuery(name = JpaConstants.USER_FIND_BY_EMAIL, query = "select u from User u where u.email = :email"),
        @NamedQuery(name = JpaConstants.USER_FIND_BY_ROLE, query = "select u from User u where u.role = :role"),
        @NamedQuery(name = JpaConstants.USER_FIND_ALL, query = "select u from User u"),
        @NamedQuery(name = JpaConstants.USER_DELETE_BY_ID, query = "delete from User u where u.id = :id")
})
public class User extends Person {

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('TENANT','DISPATCHER')")
    private Role role;

    public enum Role {
        TENANT, DISPATCHER
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof User)) { return false; }
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public static class Builder {
        private User user = new User();

        public Builder setId(int id) {
            user.setId(id);
            return this;
        }

        public Builder setName(String name) {
            user.setName(name);
            return this;
        }

        public Builder setEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder setPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder setRole(Role role) {
            user.setRole(role);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
