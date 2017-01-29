package ua.training.model.service;

import ua.training.model.entities.person.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User getUserById(int id);
    Optional<User> getUserByEmail(String email);
    User loginEmail(String email, String password);
    List<User> getAllUsers();
    void updateUser(User user, String password);
    void createNewUser(User user);
}
