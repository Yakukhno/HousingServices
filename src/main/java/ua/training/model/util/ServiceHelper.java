package ua.training.model.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import ua.training.exception.ApplicationException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.exception.ServiceException;

import java.util.function.Supplier;

@Component
public class ServiceHelper {

    private Logger logger = Logger.getLogger(ServiceHelper.class);

    public Supplier<ApplicationException> getServiceExceptionSupplier(String message) {
        return () -> {
            ApplicationException e = new ServiceException().setUserMessage(message);
            logger.info(message, e);
            return e;
        };
    }

    public Supplier<ResourceNotFoundException> getResourceNotFoundExceptionSupplier(String message,
                                                                                    int id) {
        return () -> {
            ResourceNotFoundException e = new ResourceNotFoundException();
            String fullMessage = String.format(message, id);
            logger.error(fullMessage, e);
            return e;
        };
    }
}