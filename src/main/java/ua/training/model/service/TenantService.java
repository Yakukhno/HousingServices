package ua.training.model.service;

import ua.training.model.entities.person.Tenant;

import java.util.List;

public interface TenantService {
    Tenant getTenantById(int id);
    Tenant getTenantByAccount(int account);
    Tenant getTenantByEmail(String email);
    List<Tenant> getAllTenants();
    void createNewTenant(Tenant tenant);
}
