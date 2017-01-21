package ua.training.model.service.impl;

import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.service.TypeOfWorkService;

import java.util.List;
import java.util.Optional;

public class TypeOfWorkServiceImpl implements TypeOfWorkService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private TypeOfWorkServiceImpl() {}

    private static class InstanceHolder {
        static final TypeOfWorkService INSTANCE = new TypeOfWorkServiceImpl();
    }

    public static TypeOfWorkService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<TypeOfWork> getTypeOfWorkById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TypeOfWorkDao typeOfWorkDao
                    = daoFactory.createTypeOfWorkDao(connection);
            return typeOfWorkDao.get(id);
        }
    }

    @Override
    public List<TypeOfWork> getTypeOfWorkByDescription(String string) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TypeOfWorkDao typeOfWorkDao
                    = daoFactory.createTypeOfWorkDao(connection);
            return typeOfWorkDao.getByDescription(string);
        }
    }

    @Override
    public List<TypeOfWork> getAllTypesOfWork() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TypeOfWorkDao typeOfWorkDao
                    = daoFactory.createTypeOfWorkDao(connection);
            return typeOfWorkDao.getAll();
        }
    }

    @Override
    public void createNewTypeOfWork(TypeOfWork typeOfWork) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TypeOfWorkDao typeOfWorkDao
                    = daoFactory.createTypeOfWorkDao(connection);
            typeOfWorkDao.add(typeOfWork);
        }
    }
}
