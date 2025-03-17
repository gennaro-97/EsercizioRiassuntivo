package com.example.libreria.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libri") 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    @Column(nullable = false) 
    private String titolo;

    @Column(name = "anno_pubblicazione")
    private int annoPubblicazione;

    @ManyToOne
    @JoinColumn(name = "autore_id", nullable = false) // Foreign Key nella tabella "libri"
    private Autore autore;
}