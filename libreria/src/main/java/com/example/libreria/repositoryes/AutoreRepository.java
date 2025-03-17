package com.example.libreria.repositoryes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.libreria.models.Autore;

public interface AutoreRepository extends JpaRepository<Autore, Long> {
    
}
