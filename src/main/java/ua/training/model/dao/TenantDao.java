package ua.training.model.dao;

import ua.training.model.entities.person.Tenant;

import java.util.Optional;

public interface TenantDao extends GenericDao<Tenant> {
    Optional<Tenant> getTenantByAccount(int account);
    Optional<Tenant> getTenantByEmail(String email);
}
