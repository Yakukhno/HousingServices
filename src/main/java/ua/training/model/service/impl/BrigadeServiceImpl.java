package ua.training.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.entities.Brigade;
import ua.training.model.service.BrigadeService;
import ua.training.model.util.ServiceHelper;

@Service("brigadeService")
public class BrigadeServiceImpl implements BrigadeService {

    private static final String EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND = "Brigade with id = %d not found";

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private ServiceHelper serviceHelper;

    @Override
    public Brigade getBrigadeById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            return brigadeDao.get(id).orElseThrow(
                    serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND, id));
        }
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }
}
