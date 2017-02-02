package ua.training.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TaskDto {
    private int applicationId;
    private int managerId;
    private List<Integer> workersIds;
    private LocalDateTime dateTime;

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public List<Integer> getWorkersIds() {
        return workersIds;
    }

    public void setWorkersIds(List<Integer> workersIds) {
        this.workersIds = workersIds;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static class Builder {
        TaskDto taskDto = new TaskDto();

        public Builder setApplicationId(int applicationId) {
            taskDto.setApplicationId(applicationId);
            return this;
        }

        public Builder setManagerId(int managerId) {
            taskDto.setManagerId(managerId);
            return this;
        }

        public Builder setWorkersIds(List<Integer> workersIds) {
            taskDto.setWorkersIds(workersIds);
            return this;
        }

        public Builder setDateTime(LocalDateTime dateTime) {
            taskDto.setDateTime(dateTime);
            return this;
        }

        public TaskDto build() {
            return taskDto;
        }
    }
}
