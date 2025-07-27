package com.giggi.basesetup.service;

import java.util.List;

import com.giggi.basesetup.entity.Utente;

public interface UtenteService {
    Utente save(Utente utente);

    Utente update(Utente utente);

    void deleteById(Long id);

    List<Utente> findAll();

    Utente findById(Long id);
}