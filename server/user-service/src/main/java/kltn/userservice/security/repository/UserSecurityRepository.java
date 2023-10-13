package kltn.userservice.security.repository;


import kltn.userservice.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

 }
