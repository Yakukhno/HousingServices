package ua.training.model.dao;

import ua.training.model.entities.person.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends GenericDao<User> {
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByRole(User.Role role);
}
