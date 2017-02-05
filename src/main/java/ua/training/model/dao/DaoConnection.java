package ua.training.model.dao;

public interface DaoConnection extends AutoCloseable {
    void begin();
    void commit();
    void rollback();
    void close();
    void setIsolationLevel(int isolationLevel);
}
