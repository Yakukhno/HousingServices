package ua.training.model.dao;

import ua.training.model.entities.Tenant;

public interface TenantDao extends GenericDao<Tenant> {
    Tenant getTenantByAccount(int account);
    Tenant getTenantByEmail(String email);
}
