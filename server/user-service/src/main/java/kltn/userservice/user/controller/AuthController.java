
package kltn.userservice.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kltn.userservice.common.dto.ResponseDto;
import kltn.userservice.common.entity.UserEntity;
import kltn.userservice.security.jwt.JwtUtils;
import kltn.userservice.user.dto.*;
import kltn.userservice.user.repository.UserRepository;
import kltn.userservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;
    @GetMapping("/test")
    public ResponseEntity<UserDto> geta() {
        UserEntity e = userRepository.findById(1L).orElse(null);
        UserDto userDto=new UserDto();
        userDto.setUserId(e.getId());
        userDto.setEmail(e.getEmail());
        userDto.setPhone(e.getPhone());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    /**
     * *
     * This method handles insert a user request.
     *
     * @param registerDto This is all information of the user.
     * @return A message.
     */
    @PostMapping("register")
    public ResponseEntity<ResponseDto> registerApi(@RequestBody @Valid RegisterDto registerDto) {

        this.userService.saveUser(registerDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus("REGISTER_SUCCESSES");
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("Successful registration!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles login by email and password request.
     *
     * @param loginDto This is all information to login.
     * @return A message.
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDataDto> login(@RequestBody @Valid LoginDto loginDto) {

        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("login successfully!");
        responseDataDto.setData(this.userService.loginByEmailAndPassword(loginDto));
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method to load data when login by social account
     *
     * @param socialLoginDto This is data from to save to database
     * @return A message.
     */
    @PostMapping("load-data-social-account")
    public ResponseEntity<ResponseDto> loadDataBySocialAccount(@RequestBody @Valid SocialLoginDto socialLoginDto) {

        this.userService.saveUserBySocial(socialLoginDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("Successfully retrieved data using social account!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles logout request.
     *
     * @param request This is a request from the user wants to log out.
     * @return A message.
     */
    @PostMapping("logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request) {

        // Clear session data
        request.getSession().invalidate();
        // Clear authentication
        String token = request.getHeader("Authorization");
        jwtUtils.invalidateToken(token);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("Logout successful!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * refresh token when access token expires
     *
     * @param request a refresh token from cookies
     * @return a message
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDataDto> refreshToken( HttpServletRequest request) {

        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Refresh token successfully!");
        responseDataDto.setData(this.userService.refreshToken(request));
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }
}
