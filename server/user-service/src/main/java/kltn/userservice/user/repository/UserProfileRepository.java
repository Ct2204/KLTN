

package kltn.userservice.user.repository;


import kltn.userservice.common.entity.UserProfileEntity;
import kltn.userservice.user.dto.UserProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    @Query("SELECT new kltn.userservice.user.dto.UserProfileDto(" +
            "uf.id," +
            "uf.profilePicture," +
            "uf.fullName," +
            "uf.gender," +
            "uf.birthday," +
            "u.email," +
            "u.phone)" +
            "FROM UserEntity u " +
            "INNER JOIN UserProfileEntity uf " +
            "ON u.id = uf.id")
    List<UserProfileDto> getAllUsersProfile();



    @Query("SELECT new kltn.userservice.user.dto.UserProfileDto(" +
            "uf.id," +
            "uf.profilePicture," +
            "uf.fullName," +
            "uf.gender," +
            "uf.birthday," +
            "u.email," +
            "u.phone)" +
            "FROM UserEntity u " +
            "INNER JOIN UserProfileEntity uf " +
            "ON u.id = uf.id " +
            "WHERE uf.id = :id")
    UserProfileDto getAllUserProfileById(Long id);

    @Query("SELECT new kltn.userservice.user.dto.UserProfileDto(" +
            "uf.id," +
            "uf.profilePicture," +
            "uf.fullName," +
            "uf.gender," +
            "uf.birthday," +
            "u.email," +
            "u.phone)" +
            "FROM UserEntity u " +
            "INNER JOIN UserProfileEntity uf " +
            "ON u.id = uf.id " +
            "WHERE uf.fullName LIKE %:fullName%")
    List<UserProfileDto> getAllUsersByName(String fullName);
}
