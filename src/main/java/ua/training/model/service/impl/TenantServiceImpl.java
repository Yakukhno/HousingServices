package ua.training.model.service.impl;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TenantDao;
import ua.training.model.entities.person.Tenant;
import ua.training.model.service.TenantService;

import java.util.List;

public class TenantServiceImpl implements TenantService {

    private TenantDao tenantDao = DaoFactory.getInstance().createTenantDao();

    @Override
    public Tenant getTenantByAccount(int account) {
        return tenantDao.getTenantByAccount(account);
    }

    @Override
    public Tenant getTenantByEmail(String email) {
        return tenantDao.getTenantByEmail(email);
    }

    @Override
    public Tenant getTenantById(int id) {
        return tenantDao.get(id);
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantDao.getAll();
    }

    @Override
    public void createNewTenant(Tenant tenant) {
        tenantDao.add(tenant);
    }
}
