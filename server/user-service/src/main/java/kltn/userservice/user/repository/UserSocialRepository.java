
package kltn.userservice.user.repository;


import kltn.userservice.common.entity.SocialAccountEntity;
import kltn.userservice.common.vo.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserSocialRepository extends JpaRepository<SocialAccountEntity, Integer> {

    @Query("SELECT u FROM SocialAccountEntity u WHERE u.socialAccountUserId = :socialAccountUserId")
    Optional<SocialAccountEntity> getSocialAccountUserId(String socialAccountUserId);

    @Query("SELECT u FROM SocialAccountEntity u " +
            "WHERE u.socialAccountUserId = :socialAccountUserId " +
            "and u.provider = :provider")
    Optional<SocialAccountEntity> getSocialAccountUserIdAndProvider(String socialAccountUserId, SocialProvider provider);
}
