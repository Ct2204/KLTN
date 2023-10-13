package kltn.userservice.user.controller;


import jakarta.validation.Valid;
import kltn.userservice.common.dto.ResponseDto;
import kltn.userservice.user.dto.*;
import kltn.userservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * *
     * This method handles send email with verification code request.
     *
     * @param email This is an email needs to be sent.
     *
     * @return A message.
     */
    @PostMapping("send-verification-code")
    public ResponseEntity<ResponseDto> sendVerificationCode(@RequestBody EmailDto email) {
        this.userService.sendVerificationCode(email);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("Confirmation code sent successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles confirm verification code request.
     *
     * @param verificationCode This is the verification code of the user need to be activated.
     *
     * @return The ID of the user.
     */
    @PostMapping("activate-user")
    public ResponseEntity<ResponseDto> confirmVerificationCodeAndActiveUser(@RequestBody VerificationCodeDto verificationCode) {
        this.userService.confirmVerificationCodeAndActiveUser(verificationCode);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("User is Activated Successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles confirm verification code request for change password.
     *
     * @param verificationCode This is the verification code of the user need to be activated.
     *
     * @return The email of the user.
     */
    @PostMapping("confirm-change-password")
    public ResponseDataDto confirmChangePasswordByVerificationCode(@RequestBody @Valid VerificationCodeDto verificationCode) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully confirm change password by verification code!");
        responseDataDto.setData(this
                .userService
                .confirmChangePasswordByVerificationCode(verificationCode));
        return responseDataDto;
    }

    /**
     * *
     * This method handles change password request.
     *
     * @param email           This is an email of the user.
     * @param confirmLoginDto This is a new password of the user.
     *
     * @return A message.
     */
    @PatchMapping("change-password")
    public ResponseEntity<ResponseDto> changePassword(@RequestHeader String email,
                                                 @RequestBody ConfirmLoginDto confirmLoginDto) {
        this.userService.updatePassword(email, confirmLoginDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("change password successful!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{id}/rating")
    public UserDto getUserByIdForRating(@PathVariable Long id) {
//        System.out.println("hello");
//        ResponseDataDto responseDataDto = new ResponseDataDto();
//        responseDataDto.setStatus(HttpStatus.OK.series().name());
//        responseDataDto.setCode(HttpStatus.OK.value());
//        responseDataDto.setMessage("Successfully retrieved all user addresses!");
//        responseDataDto.setData(this.userService.getUserById(id));
//        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
        UserDto userDto = userService.getUserById(id);
        return userDto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataDto> getUserById(@PathVariable Long id) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved all user addresses!");
        responseDataDto.setData(this.userService.getUserById(id));
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }




}
