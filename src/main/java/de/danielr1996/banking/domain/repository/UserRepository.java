package de.danielr1996.banking.domain.repository;


import de.danielr1996.banking.application.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public interface UserRepository extends JpaRepository<User, String> {

}
