package com.example.libreria.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.libreria.models.Autore;
import com.example.libreria.models.Libro;
import com.example.libreria.service.LibroService;
import com.example.libreria.service.AutoreService; 

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminLibroController {
    
    private final LibroService libroService;
    private final AutoreService autoreService; // Aggiungi il servizio Autore

    @GetMapping("libri")
    public String listaLibri(Model model) {
       List<Libro> libri = libroService.getLibri();
       List<Autore> autori = autoreService.getAllAutori(); // Metodo per ottenere tutti gli autori
            model.addAttribute("autori", autori); // Aggiungi gli autori al modello per il form
         model.addAttribute("libri", libri);
            return "libri"; // Assicurati che il template "libri.html" esista
    }

    // Metodo per visualizzare il form di creazione libro
    @GetMapping("/createLibroForm")
    public String creaLibroForm(Model model) {
        List<Autore> autori = autoreService.getAllAutori(); // Metodo per ottenere tutti gli autori
        model.addAttribute("autori", autori); // Aggiungi gli autori al modello per il form
        return "createLibroForm"; // Restituisce il template del form di creazione
    }

    // Metodo per salvare il libro dopo la sottomissione del form
    @PostMapping("/createLibroForm")
    public String creaLibro(@RequestParam String titolo,
                            @RequestParam int annoPubblicazione,
                            @RequestParam Long autoreId,
                            Model model) {
        // Recupera l'autore dal database
        Optional<Autore> autoreOpt = autoreService.findById(autoreId);
        if (autoreOpt.isEmpty()) {
            model.addAttribute("errore", "Autore non trovato!"); // Aggiungi messaggio errore se l'autore non esiste
            return "libri"; // Torna al form con un messaggio di errore
        }

        // Crea il libro
        Libro libro = new Libro();
        libro.setTitolo(titolo);
        libro.setAnnoPubblicazione(annoPubblicazione);
        libro.setAutore(autoreOpt.get());

        // Salva il libro nel database
        libroService.saveLibro(libro); // Usa il servizio Libro per salvare

        // Aggiungi un messaggio di successo
        model.addAttribute("successo", "Libro creato con successo!");

        // Torna alla lista dei libri
        return "redirect:/admin/libri"; // Redirect alla lista dei libri
    }

        // Metodo per eliminare un libro
    @PostMapping("/deleteLibro/{id}")
    public String deleteLibro(@PathVariable Long id) {
        libroService.deleteLibro(id); // Usa il servizio Libro per eliminare il libro
        return "redirect:/admin/libri"; // Ritorna alla pagina dei libri dopo aver eliminato il libro
    }

    // Mostra il form di modifica
    @GetMapping("/updateLibroForm/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Libro libro = libroService.getLibroById(id)
                .orElseThrow(() -> new RuntimeException("Libro non trovato con ID: " + id));

        model.addAttribute("libro", libro);
        model.addAttribute("autori", autoreService.getAllAutori()); // Per selezionare un autore dal menu a tendina
        return "updateLibroForm";
    }

    // Gestisce il salvataggio della modifica
    @PostMapping("/updateLibro/{id}")
    public String updateLibro(@PathVariable Long id, 
                              @RequestParam String titolo, 
                              @RequestParam int annoPubblicazione, 
                              @RequestParam Long autoreId) {
        libroService.updateLibro(id, titolo, annoPubblicazione, autoreId);
        return "redirect:/admin/libri"; // Dopo la modifica, torna alla lista dei libri
    }
}

