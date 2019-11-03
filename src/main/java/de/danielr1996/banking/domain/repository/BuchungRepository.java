package de.danielr1996.banking.domain.repository;

import de.danielr1996.banking.domain.entities.Buchung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
@Service
public interface BuchungRepository extends JpaRepository<Buchung, String> {
  Page<Buchung> findByKontoIdIn(Collection<UUID> kontoIds, Pageable pageable);
}
