package ua.training.model.dao;

import java.util.List;
import java.util.Optional;

import ua.training.model.entities.person.User;

public interface UserDao extends GenericDao<User> {
    Optional<User> getUserByEmail(String email);

    List<User> getUsersByRole(User.Role role);
}
