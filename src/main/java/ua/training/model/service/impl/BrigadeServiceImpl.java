package ua.training.model.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.entities.Brigade;
import ua.training.model.service.BrigadeService;

@Service("brigadeService")
public class BrigadeServiceImpl implements BrigadeService {

    private static final String EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND
            = "Brigade with id = %d not found";

    private DaoFactory daoFactory = DaoFactory.getInstance();
    private Logger logger = Logger.getLogger(BrigadeServiceImpl.class);

    @Override
    public Brigade getBrigadeById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            return brigadeDao.get(id).orElseThrow(
                    () -> {
                        ResourceNotFoundException e = new ResourceNotFoundException();
                        String message = String.format(
                                EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND, id
                        );
                        logger.info(message, e);
                        return e;
                    }
            );
        }
    }
}
