package ua.training.model.dao;

import ua.training.model.entities.person.Tenant;

public interface TenantDao extends GenericDao<Tenant> {
    Tenant getTenantByAccount(int account);
    Tenant getTenantByEmail(String email);
}
