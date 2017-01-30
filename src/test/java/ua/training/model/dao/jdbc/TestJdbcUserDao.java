package ua.training.model.dao.jdbc;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoException;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TestJdbcUserDao {

    private UserDao userDao = DaoFactory.getInstance()
            .createUserDao(mock(DaoConnection.class));

    private static List<User> usersInDb = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        usersInDb.add(new User.Builder()
                .setId(1)
                .setName("Eldon Donohoe")
                .setEmail("Hisdort@gustr.com")
                .setPassword("mvNZ)(XdP")
                .setRole(User.Role.TENANT)
                .build());
        usersInDb.add(new User.Builder()
                .setId(2)
                .setName("Tory Evers")
                .setEmail("Houry1979@teleworm.us")
                .setPassword("eLsk}_t(Z")
                .setRole(User.Role.TENANT)
                .build());
        usersInDb.add(new User.Builder()
                .setId(3)
                .setName("Jame Warren")
                .setEmail("Tootionese61@einrot.com")
                .setPassword("=a7pgaAbG")
                .setRole(User.Role.DISPATCHER)
                .build());
    }

    @Test
    public void testGetUserById() {
        assertEquals(Optional.of(usersInDb.get(1)), userDao.get(2));
    }

    @Test
    public void testGetUserByIdIncorrectId() {
        assertEquals(Optional.empty(), userDao.get(6));
    }

    @Test
    public void testGetAll() {
        assertEquals(usersInDb, userDao.getAll());
    }

    @Test
    public void testGetUserByEmail() {
        assertEquals(Optional.of(usersInDb.get(2)),
                userDao.getUserByEmail("Tootionese61@einrot.com"));
    }

    @Test
    public void testGetUserByEmailIncorrectEmail() {
        assertEquals(Optional.empty(),
                userDao.getUserByEmail("wrong@gmail.com"));
    }

    @Test
    public void testGetUserByRole() {
        List<User> users = new ArrayList<>();
        users.add(usersInDb.get(0));
        users.add(usersInDb.get(1));
        assertEquals(users, userDao.getUsersByRole(User.Role.TENANT));
    }

    @Test
    public void testAdd() {
        String email = "Suese1992@cuvox.de";
        User user = new User.Builder()
                .setName("Hiram Lovins")
                .setEmail(email)
                .setPassword("y9dP[4!:m")
                .setRole(User.Role.TENANT)
                .build();
        userDao.add(user);

        Optional<User> actualUser = userDao.getUserByEmail(email);
        actualUser.ifPresent(user1 -> userDao.delete(user1.getId()));

        assertTrue(user.getId() != 0);
        assertEquals(Optional.of(user), actualUser);
    }

    @Test(expected = DaoException.class)
    public void testAddDuplicatedEmail() {
        String email = "Houry1979@teleworm.us";
        User user = new User.Builder()
                .setName("Hiram Lovins")
                .setEmail(email)
                .setPassword("y9dP[4!:m")
                .setRole(User.Role.TENANT)
                .build();
        userDao.add(user);
    }

    @Test(expected = DaoException.class)
    public void testAddAbsentField() {
        User user = new User.Builder()
                .setEmail("Suese1992@cuvox.de")
                .setPassword("y9dP[4!:m")
                .setRole(User.Role.TENANT)
                .build();
        userDao.add(user);
    }

    @Test
    public void testUpdate() {
        User user = new User.Builder()
                .setId(2)
                .setName("Hiram Lovins")
                .setEmail("Houry1979@teleworm.us")
                .setPassword("y9dP[4!:m")
                .setRole(User.Role.TENANT)
                .build();
        userDao.update(user);

        Optional<User> actualUser = userDao.get(user.getId());
        actualUser.ifPresent(user1 -> userDao.update(usersInDb.get(1)));

        assertEquals(Optional.of(user), actualUser);
    }

    @Test(expected = DaoException.class)
    public void testUpdateDuplicatedEmail() {
        User user = new User.Builder()
                .setId(2)
                .setName("Hiram Lovins")
                .setEmail("Tootionese61@einrot.com")
                .setPassword("y9dP[4!:m")
                .setRole(User.Role.TENANT)
                .build();
        userDao.update(user);
    }
}
