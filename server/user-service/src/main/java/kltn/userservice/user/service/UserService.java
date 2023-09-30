package kltn.userservice.user.service;

import jakarta.servlet.http.HttpServletRequest;
import kltn.userservice.common.vo.SocialProvider;
import kltn.userservice.user.dto.*;

import java.util.HashMap;

public interface UserService {

    void saveUser(RegisterDto registerDto);

    HashMap<String, String> loginByEmailAndPassword(LoginDto loginDto);

    void sendVerificationCode(String email);

    void confirmVerificationCodeAndActiveUser(String verificationCode);

    void socialAccountHandler(String email, SocialProvider provider);

    void updatePassword(String email, ConfirmLoginDto confirmLoginDto);

    String confirmChangePasswordByVerificationCode(String verificationCode);

    void deleteUserById(Long id);

    void saveUserBySocial(SocialLoginDto socialLoginDto);

    HashMap<String,Object> refreshToken(HttpServletRequest request);

    UserDto getUserById(Long userId);

}
