package ua.training.model.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.training.model.entities.person.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);
    User loginEmail(String email, String password);
    List<User> getAllUsers();
    void updateUser(User user, String password);
    void createNewUser(User user);
}
