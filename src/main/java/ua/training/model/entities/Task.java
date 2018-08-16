package ua.training.model.entities;

import static ua.training.util.RepositoryConstants.TASK_APPLICATION_ID;
import static ua.training.util.RepositoryConstants.TASK_BRIGADE_ID;
import static ua.training.util.RepositoryConstants.TASK_ID;
import static ua.training.util.RepositoryConstants.TASK_IS_ACTIVE;
import static ua.training.util.RepositoryConstants.TASK_SCHEDULED_TIME;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import ua.training.util.JpaConstants;

@Entity
@NamedQueries({
        @NamedQuery(name = JpaConstants.TASK_FIND_ACTIVE, query = "select t from Task t where t.isActive = true"),
        @NamedQuery(name = JpaConstants.TASK_FIND_ALL, query = "select t from Task t"),
        @NamedQuery(name = JpaConstants.TASK_DELETE_BY_ID, query = "delete from Task t where t.id = :id")
})
public class Task {

    @Id
    @GeneratedValue
    @Column(name = TASK_ID)
    private int id;

    @OneToOne
    @JoinColumn(name = TASK_APPLICATION_ID)
    private Application application;

    @OneToOne
    @JoinColumn(name = TASK_BRIGADE_ID)
    private Brigade brigade;

    @Column(name = TASK_SCHEDULED_TIME)
    private LocalDateTime scheduledTime;

    @Column(name = TASK_IS_ACTIVE)
    private boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Brigade getBrigade() {
        return brigade;
    }

    public void setBrigade(Brigade brigade) {
        this.brigade = brigade;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", application=" + application +
                ", brigade=" + brigade +
                ", scheduledTime=" + scheduledTime +
                ", isActive=" + isActive +
                '}';
    }

    public static class Builder {
        private Task task = new Task();

        public Builder setId(int id) {
            task.setId(id);
            return this;
        }

        public Builder setApplication(Application application) {
            task.setApplication(application);
            return this;
        }

        public Builder setBrigade(Brigade brigade) {
            task.setBrigade(brigade);
            return this;
        }

        public Builder setScheduledTime(LocalDateTime scheduledTime) {
            task.setScheduledTime(scheduledTime);
            return this;
        }

        public Builder setActive(boolean isActive) {
            task.setActive(isActive);
            return this;
        }

        public Task build() {
            return task;
        }
    }
}
