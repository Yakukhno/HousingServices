package ua.training.model.service.impl;

import org.apache.log4j.Logger;
import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.entities.Brigade;
import ua.training.model.service.BrigadeService;
import ua.training.model.service.ServiceException;

import java.util.List;

public class BrigadeServiceImpl implements BrigadeService {

    private static final String EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND
            = "Brigade with id = %d not found";

    private DaoFactory daoFactory = DaoFactory.getInstance();
    private Logger logger = Logger.getLogger(BrigadeServiceImpl.class);

    private BrigadeServiceImpl() {}

    private static class InstanceHolder {
        static final BrigadeService INSTANCE = new BrigadeServiceImpl();
    }

    public static BrigadeService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Brigade getBrigadeById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            return brigadeDao.get(id).orElseThrow(
                    () -> {
                        String message = String.format(
                                EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND, id
                        );
                        ServiceException e = new ServiceException(message);
                        logger.error(message, e);
                        return e;
                    }
            );
        }
    }

    @Override
    public List<Brigade> getAllBrigades() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            return brigadeDao.getAll();
        }
    }

    @Override
    public void createNewBrigade(Brigade brigade) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            brigadeDao.add(brigade);
        }
    }
}
