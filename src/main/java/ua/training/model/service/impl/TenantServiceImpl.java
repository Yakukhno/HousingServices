package ua.training.model.service.impl;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TenantDao;
import ua.training.model.entities.person.Tenant;
import ua.training.model.service.TenantService;

import java.util.List;
import java.util.Optional;

public class TenantServiceImpl implements TenantService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private TenantServiceImpl() {}

    private static class InstanceHolder {
        static final TenantService INSTANCE = new TenantServiceImpl();
    }

    public static TenantService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<Tenant> getTenantByAccount(int account) {
        TenantDao tenantDao = daoFactory.createTenantDao();
        return tenantDao.getTenantByAccount(account);
    }

    @Override
    public Optional<Tenant> getTenantByEmail(String email) {
        TenantDao tenantDao = daoFactory.createTenantDao();
        return tenantDao.getTenantByEmail(email);
    }

    @Override
    public Optional<Tenant> getTenantById(int id) {
        TenantDao tenantDao = daoFactory.createTenantDao();
        return tenantDao.get(id);
    }

    @Override
    public Optional<Tenant> loginEmail(String email, String password) {
        TenantDao tenantDao = daoFactory.createTenantDao();
        return tenantDao.getTenantByEmail(email)
                .filter(tenant -> password.equals(tenant.getPassword()));
    }

    @Override
    public Optional<Tenant> loginAccount(int account, String password) {
        TenantDao tenantDao = daoFactory.createTenantDao();
        return tenantDao.getTenantByAccount(account)
                .filter(tenant -> password.equals(tenant.getPassword()));
    }

    @Override
    public List<Tenant> getAllTenants() {
        TenantDao tenantDao = daoFactory.createTenantDao();
        return tenantDao.getAll();
    }

    @Override
    public void createNewTenant(Tenant tenant) {
        TenantDao tenantDao = daoFactory.createTenantDao();
        tenantDao.add(tenant);
    }
}
