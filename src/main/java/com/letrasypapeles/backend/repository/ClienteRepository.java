package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.roles WHERE c.email = :email")
    Optional<Cliente> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
