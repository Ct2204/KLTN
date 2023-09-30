package kltn.userservice.user.repository;



import kltn.userservice.common.entity.UserEntity;
import kltn.userservice.common.entity.UserRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity,Long> {
    int deleteByUser(UserEntity user);
    Optional<UserRefreshTokenEntity> findByToken(String token);

}
