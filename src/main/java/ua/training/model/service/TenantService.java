package ua.training.model.service;

import ua.training.model.entities.person.Tenant;

import java.util.List;
import java.util.Optional;

public interface TenantService {
    Optional<Tenant> getTenantById(int id);
    Optional<Tenant> getTenantByAccount(int account);
    Optional<Tenant> getTenantByEmail(String email);
    Optional<Tenant> login(String email, String password);
    List<Tenant> getAllTenants();
    void createNewTenant(Tenant tenant);
}
