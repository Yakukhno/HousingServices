package ua.training.model.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    private static final String EMAIL = "a@a.com";
    private static final String NOT_EXISTING_EMAIL = "a1@a.com";
    private static final String PASSWORD = "qwerty";

    @Mock
    private UserDao userDaoMock;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void shouldLoadUserByUsername() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        when(userDaoMock.getUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(EMAIL);

        assertEquals(EMAIL, result.getUsername());
        assertEquals(PASSWORD, result.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionWhenLoadUserByUsernameIfUserIsNotFound() {
        when(userDaoMock.getUserByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        userDetailsService.loadUserByUsername(NOT_EXISTING_EMAIL);
    }
}