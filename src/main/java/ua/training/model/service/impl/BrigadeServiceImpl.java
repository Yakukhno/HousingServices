package ua.training.model.service.impl;

import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TenantDao;
import ua.training.model.entities.Brigade;
import ua.training.model.service.BrigadeService;

import java.util.List;

public class BrigadeServiceImpl implements BrigadeService {

    private BrigadeDao brigadeDao = DaoFactory.getInstance().createBrigadeDao();

    @Override
    public Brigade getBrigadeById(int id) {
        return brigadeDao.get(id);
    }

    @Override
    public List<Brigade> getAllBrigades() {
        return brigadeDao.getAll();
    }

    @Override
    public void createNewBrigade(Brigade brigade) {
        brigadeDao.add(brigade);
    }
}
