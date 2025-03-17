package com.example.libreria.controllers;

import com.example.libreria.models.Utente;
import com.example.libreria.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



/**
 * Controller per la gestione della registrazione e del login degli utenti.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtenteService userService;

    /**
     * Mostra il modulo di registrazione.
     *
     * @param model Model per passare dati alla vista
     * @return Nome del template Thymeleaf per la registrazione
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Utente());
        return "register";
    }

    /**
     * Gestisce la registrazione di un nuovo utente.
     *
     * @param user Oggetto User ottenuto dal form di registrazione
     * @return Redirect alla pagina di login
     */
    @PostMapping("/register")
    public String register(@ModelAttribute Utente user) {
        try {
            userService.register(user);
            return "redirect:/auth/login";
        } catch (Exception e) {
            e.printStackTrace(); // Stampa l'errore
            return "error"; // Mostra una pagina di errore
        }
    }
    

    /**
     * Mostra il modulo di login.
     *
     * @return Nome del template Thymeleaf per il login
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
