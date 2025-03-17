package com.example.libreria.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.libreria.models.Libro;
import com.example.libreria.repositoryes.LibroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibroService {

    private final LibroRepository libroRepository;

    // Recupera tutti i libri dal db
    public List<Libro> getLibri() {
        return libroRepository.findAll();
    }


    // Metodo per salvare il libro
    public Libro saveLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    public void deleteLibro(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro non trovato con ID: " + id);
        }
        libroRepository.deleteById(id);
    }

}
