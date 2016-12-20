package ua.training.model.dao;

import java.io.IOException;
import java.util.Properties;

public abstract class DaoFactory {
    public static final String PROPERTIES_FILE = "/db.properties";
    public static final String FACTORY_CLASS = "factory.class";

    public static DaoFactory daoFactory;

    public abstract ApplicationDAO createApplicationDao();
    public abstract TenantDao createTenantDao();
    public abstract WorkerDao createWorkerDao();
    public abstract WorkDao createWorkDao();

    public DaoFactory getInstance() {
        if (daoFactory == null) {
            try {
                Properties properties = new Properties();
                properties.load(DaoFactory.class.getResourceAsStream(PROPERTIES_FILE));
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
