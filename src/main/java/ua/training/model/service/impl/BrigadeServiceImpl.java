package ua.training.model.service.impl;

import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.DaoFactory;
import ua.training.model.entities.Brigade;
import ua.training.model.service.BrigadeService;

import java.util.List;
import java.util.Optional;

public class BrigadeServiceImpl implements BrigadeService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private BrigadeServiceImpl() {}

    private static class InstanceHolder {
        static final BrigadeService INSTANCE = new BrigadeServiceImpl();
    }

    public static BrigadeService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<Brigade> getBrigadeById(int id) {
        BrigadeDao brigadeDao = daoFactory.createBrigadeDao();
        return brigadeDao.get(id);
    }

    @Override
    public List<Brigade> getAllBrigades() {
        BrigadeDao brigadeDao = daoFactory.createBrigadeDao();
        return brigadeDao.getAll();
    }

    @Override
    public void createNewBrigade(Brigade brigade) {
        BrigadeDao brigadeDao = daoFactory.createBrigadeDao();
        brigadeDao.add(brigade);
    }
}
