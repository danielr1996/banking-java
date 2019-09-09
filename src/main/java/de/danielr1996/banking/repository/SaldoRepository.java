package de.danielr1996.banking.repository;

import de.danielr1996.banking.domain.entities.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Repository
@Service
public interface SaldoRepository extends JpaRepository<Saldo, UUID> {

}
