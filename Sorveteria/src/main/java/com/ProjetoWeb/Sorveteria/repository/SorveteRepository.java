package com.ProjetoWeb.Sorveteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ProjetoWeb.Sorveteria.model.Sorvete;

import java.util.List;

@Repository
public interface SorveteRepository extends JpaRepository<Sorvete, Long> {

 
    List<Sorvete> findByIsDeletedIsNull();


    List<Sorvete> findByIsDeletedIsNotNull();
}
