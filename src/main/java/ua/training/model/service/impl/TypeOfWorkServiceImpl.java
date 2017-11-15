package ua.training.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.service.TypeOfWorkService;

@Service("typeOfWorkService")
public class TypeOfWorkServiceImpl implements TypeOfWorkService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public List<TypeOfWork> getAllTypesOfWork() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TypeOfWorkDao typeOfWorkDao = daoFactory.createTypeOfWorkDao(connection);
            return typeOfWorkDao.getAll();
        }
    }
}
