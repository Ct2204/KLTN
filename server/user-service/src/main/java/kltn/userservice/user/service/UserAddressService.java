
package kltn.userservice.user.service;



import kltn.userservice.user.dto.UpdatingUserAddressDto;
import kltn.userservice.user.dto.UserAddressDto;

import java.util.List;

public interface UserAddressService {

    List<UserAddressDto> getAllUsersAddresses();

    List<UserAddressDto> getAllUserAddressesByUserId(Long id);

    UserAddressDto getAllUserAddressById(Long id);

    void updateUserAddress(Long id, UpdatingUserAddressDto userAddressRequest);
}
