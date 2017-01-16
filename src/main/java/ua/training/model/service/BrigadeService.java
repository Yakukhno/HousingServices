package ua.training.model.service;

import ua.training.model.entities.Brigade;

import java.util.List;

public interface BrigadeService {
    Brigade getBrigadeById(int id);
    List<Brigade> getAllBrigades();
    void createNewBrigade(Brigade brigade);
}
