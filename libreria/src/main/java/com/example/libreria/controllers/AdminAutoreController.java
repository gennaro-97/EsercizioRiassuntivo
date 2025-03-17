package com.example.libreria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.libreria.models.Autore;
import com.example.libreria.service.AutoreService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAutoreController {

    private final AutoreService autoreService;

    // Metodo per visualizzare il form di creazione autore
    @GetMapping("/createAutoreForm")
    public String creaAutoreForm(Model model) {
        return "createAutoreForm"; // Restituisce il template del form di creazione autore
    }

    // Metodo per salvare l'autore dopo la sottomissione del form
    @PostMapping("/createAutoreForm")
    public String creaAutore(@RequestParam String nome,
            @RequestParam String cognome,
            Model model) {

        // Crea l'autore
        Autore autore = new Autore();
        autore.setNome(nome);
        autore.setCognome(cognome);

        // Salva l'autore nel database
        autoreService.saveAutore(autore); // Usa il servizio Autore per salvare

        // Aggiungi un messaggio di successo
        model.addAttribute("successo", "Autore creato con successo!");

        // Torna alla lista degli autori
        return "redirect:/admin/libri"; // Redirect alla lista dei libri, se necessario
    }

    // Metodo per eliminare un autore
    @PostMapping("/deleteAutore/{id}")
    public String deleteAutore(@PathVariable Long id) {
        autoreService.deleteAutore(id); // Usa il servizio Autore per eliminare l'autore
        return "redirect:/admin/libri"; // Ritorna alla pagina dei libri dopo aver eliminato l'autore
    }

    // Mostra il form di modifica dell'autore
@GetMapping("/updateAutoreForm/{id}")
public String showUpdateAutoreForm(@PathVariable Long id, Model model) {
    Autore autore = autoreService.findById(id)
            .orElseThrow(() -> new RuntimeException("Autore non trovato con ID: " + id));

    model.addAttribute("autore", autore);
    return "updateAutoreForm"; // Nome del template HTML che contiene il form di modifica
}

// Gestisce il salvataggio della modifica dell'autore
@PostMapping("/updateAutore/{id}")
public String updateAutore(@PathVariable Long id, 
                           @RequestParam String nome, 
                           @RequestParam String cognome) {
    autoreService.updateAutore(id, nome, cognome);
    return "redirect:/admin/libri"; // Dopo la modifica, torna alla lista degli autori
}


}
