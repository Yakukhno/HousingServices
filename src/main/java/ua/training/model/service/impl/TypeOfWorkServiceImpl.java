package ua.training.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.service.TypeOfWorkService;

@Service("typeOfWorkService")
public class TypeOfWorkServiceImpl implements TypeOfWorkService {

    private TypeOfWorkDao typeOfWorkDao;

    @Override
    public List<TypeOfWork> getAllTypesOfWork() {
        return typeOfWorkDao.getAll();
    }

    public TypeOfWorkDao getTypeOfWorkDao() {
        return typeOfWorkDao;
    }

    @Autowired
    public void setTypeOfWorkDao(TypeOfWorkDao typeOfWorkDao) {
        this.typeOfWorkDao = typeOfWorkDao;
    }
}
