package ua.training.model.dao;

import java.io.IOException;
import java.util.Properties;

public abstract class DaoFactory {
    private static final String DB_PROPERTIES_FILE = "/db.properties";
    private static final String FACTORY_CLASS = "factory.class";

    private static DaoFactory daoFactory;

    public abstract DaoConnection getConnection();

    public abstract ApplicationDao createApplicationDao(DaoConnection daoConnection);
    public abstract TenantDao createTenantDao(DaoConnection daoConnection);
    public abstract DispatcherDao createDispatcherDao(DaoConnection daoConnection);
    public abstract WorkerDao createWorkerDao(DaoConnection daoConnection);
    public abstract BrigadeDao createBrigadeDao(DaoConnection daoConnection);
    public abstract TaskDao createTaskDao(DaoConnection daoConnection);
    public abstract TypeOfWorkDao createTypeOfWorkDao(DaoConnection daoConnection);

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
