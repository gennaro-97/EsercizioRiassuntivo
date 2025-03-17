package com.example.libreria.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.libreria.models.Autore;
import com.example.libreria.models.Libro;
import com.example.libreria.repositoryes.AutoreRepository;
import com.example.libreria.repositoryes.LibroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutoreRepository autoreRepository;

    // Recupera tutti i libri dal db
    public List<Libro> getLibri() {
        return libroRepository.findAll();
    }


    public Optional<Libro> getLibroById(Long id) {
        return libroRepository.findById(id);
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

    // Metodo per aggiornare un libro
    public Libro updateLibro(Long id, String titolo, int annoPubblicazione, Long autoreId) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro non trovato con ID: " + id));

        Autore autore = autoreRepository.findById(autoreId)
                .orElseThrow(() -> new RuntimeException("Autore non trovato con ID: " + autoreId));

        // Aggiorna i campi del libro
        libro.setTitolo(titolo);
        libro.setAnnoPubblicazione(annoPubblicazione);
        libro.setAutore(autore);

        return libroRepository.save(libro);
    }
}
