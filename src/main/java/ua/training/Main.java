package ua.training;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TenantDao;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.Application;
import ua.training.model.entities.Tenant;
import ua.training.model.entities.TypeOfWork;

import java.sql.Time;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        TenantDao tenantDao = daoFactory.createTenantDao();
        Tenant tenant = new Tenant.Builder()
                .setAccount(1006)
                .setName("Gerry Jantzen")
                .setEmail("Hicarrim1935@cuvox.de")
                .setPassword("s?M-L:2)6")
                .build();
//        tenantDao.add(tenant);
//        System.out.println(tenant);

        tenantDao.delete(5);

        ApplicationDao applicationDao = daoFactory.createApplicationDao();
//        System.out.println(applicationDao.getApplicationsByTypeOfWork("supply"));

        TypeOfWorkDao typeOfWorkDao = daoFactory.createTypeOfWorkDao();

//        Application application = new Application();
//        tenant.setId(3);
//        application.setTenant(tenant);
//        application.setTypeOfWork("Gas supply");
//        application.setScaleOfProblem("section");
//        application.setDesiredTime(new Timestamp(System.currentTimeMillis()));
//        applicationDao.add(application);

//        System.out.println(application);
//        applicationDao.delete(11);
    }
}
