package com.giggi.basesetup.controller;

import com.giggi.basesetup.dto.response.utente.UtenteFindAllDTO;
import com.giggi.basesetup.mapper.UtenteMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.giggi.basesetup.entity.Utente;
import com.giggi.basesetup.service.UtenteService;

@RestController
@RequestMapping("/api/utentes")
@RequiredArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;
    private final UtenteMapper utenteMapper;

    @GetMapping
    public ResponseEntity<UtenteFindAllDTO> getAllUtentes() {
        return ResponseEntity.ok(
                new UtenteFindAllDTO(
                        utenteService
                                .findAll().stream()
                                .map(utenteMapper::conver)
                                .toList())
        );
    }
}