package ua.training.model.dao;

import ua.training.model.entities.TypeOfWork;

import java.io.IOException;
import java.util.Properties;

public abstract class DaoFactory {
    private static final String DB_PROPERTIES_FILE = "/db.properties";
    private static final String FACTORY_CLASS = "factory.class";

    private static DaoFactory daoFactory;

    public abstract ApplicationDao createApplicationDao();
    public abstract TenantDao createTenantDao();
    public abstract WorkerDao createWorkerDao();
    public abstract WorkDao createWorkDao();
    public abstract TypeOfWorkDao createTypeOfWorkDao();

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            try {
                Properties properties = new Properties();
                properties.load(DaoFactory.class
                        .getResourceAsStream(DB_PROPERTIES_FILE));
                daoFactory = (DaoFactory) Class.forName(properties
                        .getProperty(FACTORY_CLASS)).newInstance();
            } catch (IOException | ClassNotFoundException |
                    IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return daoFactory;
    }
}
