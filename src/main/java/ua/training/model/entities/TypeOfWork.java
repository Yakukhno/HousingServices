package ua.training.model.entities;

import java.util.Objects;

public class TypeOfWork {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeOfWork that = (TypeOfWork) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return "TypeOfWork{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
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
