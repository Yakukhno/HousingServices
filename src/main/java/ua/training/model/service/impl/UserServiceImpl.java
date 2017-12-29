package ua.training.model.service.impl;

import static ua.training.util.RoleConstants.ROLE_DISPATCHER;

import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.util.ServiceHelper;
import ua.training.util.ExceptionConstants;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND = "User with id = %d not found";
    private static final int PASS_MAX_LENGTH = 64;

    private UserDao userDao;
    private ServiceHelper serviceHelper;

    @Override
    @Secured("IS_AUTHENTICATED_FULLY")
    @PreAuthorize("#id == principal.user.id")
    public User getUserById(int id) {
        return userDao.get(id)
                .orElseThrow(serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_USER_WITH_ID_NOT_FOUND, id));
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public UserServiceImpl() {
        super();
    }

    @Override
    @Secured("IS_AUTHENTICATED_FULLY")
    @PreAuthorize("#user.id == principal.user.id")
    @Transactional
    public void updateUser(User user, String password) {
        String hashedPassword = DigestUtils.sha256Hex(password);
        hashNewPassword(user);

        Optional<User> userFromDao = userDao.get(user.getId());
        userFromDao.orElseThrow(
                serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_USER_WITH_ID_NOT_FOUND, user.getId()));
        userFromDao.filter(user1 -> hashedPassword.equals(user1.getPassword()))
                .orElseThrow(serviceHelper.getServiceExceptionSupplier(ExceptionConstants.INCORRECT_PASSWORD));
        userDao.update(user);
    }

    private void hashNewPassword(User user) {
        if (user.getPassword().length() < PASS_MAX_LENGTH) {
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        }
    }

    @Override
    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    public void createNewUser(User user) {
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        userDao.add(user);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }
}
