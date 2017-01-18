package ua.training.model.service;

import ua.training.model.entities.Brigade;

import java.util.List;
import java.util.Optional;

public interface BrigadeService {
    Optional<Brigade> getBrigadeById(int id);
    List<Brigade> getAllBrigades();
    void createNewBrigade(Brigade brigade);
}
