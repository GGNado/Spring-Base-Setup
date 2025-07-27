package com.giggi.basesetup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.basesetup.entity.Utente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    @Query("SELECT u FROM Utente u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<Utente> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    Optional<Utente> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}