package ua.training.model.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.model.UserDetailsImpl;

import java.util.function.Supplier;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND
            = "User with email = %s not found";

    private DaoFactory daoFactory;

    public UserDetailsServiceImpl() {
        daoFactory = DaoFactory.getInstance();
    }

    UserDetailsServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            User user = userDao.getUserByEmail(email)
                    .orElseThrow(getUsernameNotFoundExceptionSupplier(email));
            return new UserDetailsImpl(user);
        }
    }

    private Supplier<UsernameNotFoundException>
                getUsernameNotFoundExceptionSupplier(String email) {
        return () -> new UsernameNotFoundException(
                String.format(EXCEPTION_USER_WITH_ID_NOT_FOUND, email)
        );
    }
}
