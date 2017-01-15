package ua.training;

import ua.training.model.dao.*;
import ua.training.model.dao.jdbc.JdbcDispatcherDao;
import ua.training.model.entities.*;
import ua.training.model.entities.person.Dispatcher;

import java.sql.Time;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        TenantDao tenantDao = daoFactory.createTenantDao();
//        Tenant tenant = new Tenant.Builder()
//                .setAccount(1006)
//                .setName("Gerry Jantzen")
//                .setEmail("Hicarrim1935@cuvox.de")
//                .setPassword("s?M-L:2)6")
//                .build();
//        tenantDao.add(tenant);
//        System.out.println(tenantDao.getAll());

//        tenantDao.delete(5);

        ApplicationDao applicationDao = daoFactory.createApplicationDao();
//        System.out.println(applicationDao.getApplicationsByTypeOfWork("supply"));

        TypeOfWorkDao typeOfWorkDao = daoFactory.createTypeOfWorkDao();

//        Application application = new Application();
//        tenant.setId(3);
//        application.setTenant(tenant);
//        application.setTypesOfWork(typeOfWorkDao.getByDescription("gas supply").get(0));
//        application.setScaleOfProblem(ProblemScale.SECTION);
//        application.setDesiredTime(new Timestamp(System.currentTimeMillis()));
//        applicationDao.add(application);

//        System.out.println(application);
//        applicationDao.delete(11);
//        System.out.println(applicationDao.getAll());

        WorkerDao workerDao = daoFactory.createWorkerDao();
//        Worker worker = new Worker.Builder()
//                .setName("Harry Kane")
//                .addTypeOfWork(typeOfWorkDao.getByDescription("supply").get(0))
//                .addTypeOfWork(typeOfWorkDao.getByDescription("supply").get(1))
//                .build();
//        System.out.println(workerDao.getAll());

        BrigadeDao brigadeDao = daoFactory.createBrigadeDao();
//        System.out.println(brigadeDao.getAll());

        DispatcherDao dispatcherDao = daoFactory.createDispatcherDao();
//        Dispatcher dispatcher = new Dispatcher.Builder()
//                .setOnline(true)
//                .setName("Gabriela Trater")
//                .setEmail("gabi_Trater90@dayrep.com")
//                .setPassword("2An-6N:99")
//                .build();
//        dispatcherDao.delete(3);

        System.out.println(dispatcherDao.getAll());
    }
}
