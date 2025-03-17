package com.example.libreria.service;

import com.example.libreria.models.Autore;
import com.example.libreria.models.Libro;
import com.example.libreria.repositoryes.AutoreRepository;
import com.example.libreria.repositoryes.LibroRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutoreService {

    private final AutoreRepository autoreRepository; // Iniezione della repository degli autori
    private final LibroRepository libroRepository; // Iniezione della repository dei libri

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

        // Metodo per aggiornare un Autore
   /*  public Autore updateAutore(Long id, String nome, String cognome) {
        Autore autore = autoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autore non trovato con ID: " + id));

        // Aggiorna i campi del libro
        autore.setNome(nome);
        autore.setCognome(cognome);

        return autoreRepository.save(autore);
    } */

    public Autore updateAutore(Long id, String nome, String cognome) {
    Autore autore = autoreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Autore non trovato con ID: " + id));

    // Aggiorna i campi dell'autore
    autore.setNome(nome);
    autore.setCognome(cognome);

    // Aggiorna tutti i libri associati a questo autore
    List<Libro> libri = libroRepository.findByAutore(autore); // Supponendo che ci sia un metodo che trova i libri per autore
    for (Libro libro : libri) {
        libro.setAutore(autore);  // Aggiorna il riferimento all'autore
        libroRepository.save(libro); // Salva il libro aggiornato
    }

    // Salva l'autore aggiornato
    return autoreRepository.save(autore);
}

}

