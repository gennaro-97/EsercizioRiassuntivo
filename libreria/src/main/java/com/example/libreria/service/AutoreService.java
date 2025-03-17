package com.example.libreria.service;

import com.example.libreria.models.Autore;
import com.example.libreria.repositoryes.AutoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutoreService {

    private final AutoreRepository autoreRepository; // Iniezione della repository degli autori

    // Metodo per ottenere tutti gli autori
    public List<Autore> getAllAutori() {
        return autoreRepository.findAll(); // Trova tutti gli autori nel database
    }

    // Metodo per trovare un autore per ID
    public Optional<Autore> findById(Long id) {
        return autoreRepository.findById(id); // Trova l'autore per ID
    }

        // Metodo per salvare il libro
    public Autore saveAutore(Autore autore) {
        return autoreRepository.save(autore);
    }

    public void deleteAutore(Long id) {
        if (!autoreRepository.existsById(id)) {
            throw new RuntimeException("Autore non trovato con ID: " + id);
        }
        autoreRepository.deleteById(id);
    }
}

