package ua.training.model.entities;

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
    public String toString() {
        return "TypeOfWork{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

    public static class Builder {
        private TypeOfWork typeOfWork = new TypeOfWork();

        public Builder setId(int id) {
            typeOfWork.id = id;
            return this;
        }

        public Builder setDescription(String description) {
            typeOfWork.description = description;
            return this;
        }

        public TypeOfWork build() {
            return typeOfWork;
        }
    }
}
