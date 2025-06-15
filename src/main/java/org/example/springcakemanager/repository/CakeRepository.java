package org.example.springcakemanager.repository;

import io.swagger.v3.oas.annotations.Hidden;
import org.example.springcakemanager.model.CakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface CakeRepository extends JpaRepository<CakeEntity, Long> {
}

