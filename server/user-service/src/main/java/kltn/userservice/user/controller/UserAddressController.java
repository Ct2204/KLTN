
package kltn.userservice.user.controller;


import jakarta.validation.Valid;
import kltn.userservice.common.dto.ResponseDto;
import kltn.userservice.user.dto.ResponseDataDto;
import kltn.userservice.user.dto.UpdatingUserAddressDto;
import kltn.userservice.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users/address")
public class UserAddressController {

    @Autowired
    UserAddressService userAddressService;

    /**
     * *
     * This method handles get all users addresses request.
     *
     * @return A list of users addresses.
     */
    @GetMapping("")
    public ResponseEntity<ResponseDataDto> getAllUsersAddresses() {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved all user addresses!");
        responseDataDto.setData(this.userAddressService.getAllUsersAddresses());
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles get all user addresses by user ID request.
     *
     * @param id This is an ID of the user.
     *
     * @return All addresses of the user.
     */
    @GetMapping("/by-user/{id}")
    public ResponseEntity<ResponseDataDto> getAllUserAddressesByUserId(@PathVariable("id") Long id) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved all user addresses by user ID!");
        responseDataDto.setData(this.userAddressService.getAllUserAddressesByUserId(id));
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles get a user address by ID request.
     *
     * @param id This is an ID of the address.
     *
     * @return The address of the user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataDto> getAllUserAddressById(@PathVariable("id") Long id) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved all user addresses by address ID!");
        responseDataDto.setData(this.userAddressService.getAllUserAddressById(id));
        return ResponseEntity.ok(responseDataDto);
    }

    /**
     * *
     * This method handles update a user address.
     *
     * @param id                 This is an ID of the address.
     * @param userAddressRequest This is all information to update a user address.
     *
     * @return A message 'Updated successfully!'.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto> updateUserAddress(@PathVariable("id") Long id,
                                                         @RequestBody @Valid UpdatingUserAddressDto userAddressRequest) {
        this.userAddressService.updateUserAddress(id, userAddressRequest);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("Address update successful!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
