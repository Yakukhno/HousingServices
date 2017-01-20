package ua.training.model.service;

import ua.training.model.entities.person.User;

import java.util.Optional;

public interface UserService {
    Optional<User> loginEmail(String email, String password);
    Optional<User> loginAccount(int account, String password);
}
