package com.example.libreria.repositoryes;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.libreria.models.Autore;
import com.example.libreria.models.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    List<Libro> findByAutoreId(Long autoreId);
    List<Libro> findByAutore(Autore autore);
}
