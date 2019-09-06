package de.danielr1996.banking.repository;

import de.danielr1996.banking.domain.Buchung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Repository
@Service
public interface BuchungRepository extends JpaRepository<Buchung, String> {

}
