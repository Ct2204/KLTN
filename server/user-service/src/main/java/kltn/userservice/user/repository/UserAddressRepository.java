
package kltn.userservice.user.repository;


import kltn.userservice.common.entity.UserAddressEntity;
import kltn.userservice.user.dto.UserAddressDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

    @Query("SELECT new kltn.userservice.user.dto.UserAddressDto(" +
            "u.id, u.fullName, u.company, u.phone," +
            "u.region, u.district, u.ward," +
            "u.street, u.status)" +
            "FROM UserAddressEntity u " +
            "WHERE u.user.id = :id")
    List<UserAddressDto> getAllUserAddressesByUserId(Long id);

    @Transactional
    @Modifying
    @Query(value = "update user_address set status = 0 where user_id = :id and status = 1", nativeQuery = true)
    void updateUserAddressesByStatus(Long id);
}
