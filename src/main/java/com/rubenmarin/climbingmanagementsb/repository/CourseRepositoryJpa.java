package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


//Spring Data nos proporciona automáticamente métodos como:
//findAll(), findById(), save() ,deleteById(), existsById(), count()
public interface CourseRepositoryJpa extends JpaRepository<CourseEntity, Long> {
    // Derived Query:
    // Spring Data JPA interpreta el nombre del método y genera automáticamente
    // la implementación de la consulta.
    // findByDifficulty(Difficulty difficulty)
    //     ↓
    // busca la propiedad "difficulty" de CourseEntity
    //     ↓
    // genera conceptualmente:
    // SELECT * FROM courses WHERE difficulty = ?
    //
    // No es necesario implementar este método manualmente.
    List<CourseEntity> findByDifficulty(Difficulty difficulty);

    //Spring Data JPA analiza el nombre del método y construye la consulta.
    Optional<CourseEntity> findTopByOrderByPriceDesc();

    //Spring interpreta:Top OrderBy   Price Desc
    /// /////////////////// Derived Query
    List<CourseEntity> findByDifficultyAndPriceLessThan(Difficulty difficulty, Double price);

    /// ///////////////////////// @Query + JPQL
    @Query("""
        SELECT c
        FROM CourseEntity c
        WHERE c.difficulty = :difficulty
        AND c.price < :price
        """)
    List<CourseEntity> searchCourses(
            @Param("difficulty") Difficulty difficulty,
            @Param("price") Double price
    );
}

