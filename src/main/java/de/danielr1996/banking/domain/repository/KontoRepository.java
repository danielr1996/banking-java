package de.danielr1996.banking.domain.repository;


import java.util.List;
import java.util.UUID;
import de.danielr1996.banking.domain.entities.Konto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public interface KontoRepository extends JpaRepository<Konto, UUID> {
  List<Konto> findByUserId(String userId);
}
