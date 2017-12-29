package ua.training.model.service.impl;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.training.model.UserDetailsImpl;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND = "User with email = %s not found";

    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(email).orElseThrow(getUsernameNotFoundExceptionSupplier(email));
        return new UserDetailsImpl(user);
    }

    private Supplier<UsernameNotFoundException> getUsernameNotFoundExceptionSupplier(String email) {
        return () -> new UsernameNotFoundException(String.format(EXCEPTION_USER_WITH_ID_NOT_FOUND, email));
    }

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
