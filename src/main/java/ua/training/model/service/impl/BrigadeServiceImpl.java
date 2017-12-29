package ua.training.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.training.model.dao.BrigadeDao;
import ua.training.model.entities.Brigade;
import ua.training.model.service.BrigadeService;
import ua.training.model.util.ServiceHelper;

@Service("brigadeService")
public class BrigadeServiceImpl implements BrigadeService {

    private static final String EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND = "Brigade with id = %d not found";

    private BrigadeDao brigadeDao;

    private ServiceHelper serviceHelper;

    @Override
    public Brigade getBrigadeById(int id) {
        return brigadeDao.get(id).orElseThrow(
                serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_BRIGADE_WITH_ID_NOT_FOUND, id));
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }

    public BrigadeDao getBrigadeDao() {
        return brigadeDao;
    }

    @Autowired
    public void setBrigadeDao(BrigadeDao brigadeDao) {
        this.brigadeDao = brigadeDao;
    }
}
