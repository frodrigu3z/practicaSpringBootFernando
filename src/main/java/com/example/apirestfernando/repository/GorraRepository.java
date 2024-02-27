package com.example.apirestfernando.repository;
import com.example.apirestfernando.model.Gorra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GorraRepository extends JpaRepository<Gorra, Integer> {
}