package ua.training.model.service.impl;

import ua.training.model.dao.DaoConnection;
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
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            return tenantDao.getTenantByAccount(account);
        }
    }

    @Override
    public Optional<Tenant> getTenantByEmail(String email) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            return tenantDao.getTenantByEmail(email);
        }
    }

    @Override
    public Optional<Tenant> getTenantById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            return tenantDao.get(id);
        }
    }

    @Override
    public Optional<Tenant> loginEmail(String email, String password) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            return tenantDao.getTenantByEmail(email)
                    .filter(tenant -> password.equals(tenant.getPassword()));
        }
    }

    @Override
    public Optional<Tenant> loginAccount(int account, String password) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            return tenantDao.getTenantByAccount(account)
                    .filter(tenant -> password.equals(tenant.getPassword()));
        }
    }

    @Override
    public List<Tenant> getAllTenants() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            return tenantDao.getAll();
        }
    }

    @Override
    public void updateTenant(Tenant tenant) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            tenantDao.update(tenant);
        }
    }

    @Override
    public void createNewTenant(Tenant tenant) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TenantDao tenantDao = daoFactory.createTenantDao(connection);
            tenantDao.add(tenant);
        }
    }
}
