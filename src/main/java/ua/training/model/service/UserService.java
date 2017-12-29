package ua.training.model.service;

import java.util.List;

import ua.training.model.entities.person.User;

public interface UserService {
    User getUserById(int id);

    List<User> getAllUsers();

    void updateUser(User user, String password);

    void createNewUser(User user);
}
