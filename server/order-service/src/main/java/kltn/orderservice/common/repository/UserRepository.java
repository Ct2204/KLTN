
package kltn.orderservice.common.repository;

import kltn.orderservice.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity  u WHERE u.email = ?1")
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity  u WHERE u.verificationCode = ?1")
    Optional<UserEntity> findByVerificationCode(String verificationCode);

    Optional<UserEntity> findOneById(Long id);
}
