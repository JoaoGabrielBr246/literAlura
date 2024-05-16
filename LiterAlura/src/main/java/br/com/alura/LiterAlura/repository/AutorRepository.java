package br.com.alura.LiterAlura.repository;

import br.com.alura.LiterAlura.model.Autor;
import br.com.alura.LiterAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Livro, Long> {
    @Query("SELECT a FROM Autor a WHERE a.death_year IS NULL OR a.death_year > :ano")
    List<Autor> buscarAutoresVivosNoAno(@Param("ano") Integer ano);
}
