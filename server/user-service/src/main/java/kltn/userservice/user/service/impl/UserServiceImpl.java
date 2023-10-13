package kltn.userservice.user.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import kltn.userservice.common.entity.SocialAccountEntity;
import kltn.userservice.common.entity.UserEntity;
import kltn.userservice.common.entity.UserProfileEntity;
import kltn.userservice.common.exception.*;
import kltn.userservice.common.vo.Gender;
import kltn.userservice.common.vo.SocialProvider;
import kltn.userservice.common.vo.UserStatusEnum;
import kltn.userservice.security.jwt.AuthRefreshToken;
import kltn.userservice.security.jwt.JwtUtils;
import kltn.userservice.user.dto.*;
import kltn.userservice.user.repository.*;
import kltn.userservice.user.service.UserService;
import kltn.userservice.user.validator.EmailAlreadyExistException;
import kltn.userservice.user.validator.InvalidEmailOrPasswordException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserSocialRepository userSocialRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthRefreshToken authRefreshToken;

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;
    /**
     * *
     * Login by email and password.
     *
     * @param loginDto This is all information of the user.
     * @throws ResourceNotFoundException    if the user with email not found.
     * @throws InternalServerErrorException if the user is not activated.
     */
    @Override
    public HashMap<String, String> loginByEmailAndPassword(LoginDto loginDto) {
        UserEntity user = this.userRepository
                .findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + loginDto.getEmail() + " not found!"));
        if (user.getStatus() == UserStatusEnum.NOT_ACTIVATED) {
            throw new NotActivatedExceptionHandler("Your account is not activated!");
        }

        if(user.getStatus() == UserStatusEnum.BANNED) {
            throw new AuthorizeException("This account has been banned!");
        }

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new InvalidEmailOrPasswordException("Invalid email token!");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HashMap<String, String> dataHashmap = new HashMap<>();
        dataHashmap.put("access_token", jwtUtils.generateTokenFromUsername(loginDto.getEmail()));
        dataHashmap.put("user_id", String.valueOf(user.getId()));
        if(user.getSeller() == null){
            dataHashmap.put("seller_id", null);
        }else {
            dataHashmap.put("seller_id", String.valueOf(user.getSeller().getId()));
        }
        String refreshToken = this.authRefreshToken.generateRefreshToken(loginDto.getEmail());
        dataHashmap.put("refresh_token",refreshToken);
        return dataHashmap;
    }

    /**
     * *
     * Insert a user.
     *
     * @param registerDto This is all information of the user.
     * @throws DuplicateResourceException if email has been taken.
     */

    @Override
    public void saveUser(RegisterDto registerDto) {

        //Checking if the email is taken by social account or not
        if (this.userSocialRepository.getSocialAccountUserId(registerDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email has registered by social account!");
        }

        //Checking if the email is taken by users
        if (this.userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email has been taken!");
        }

        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Invalid confirm password!");
        }

        //Creating user
        UserEntity user = mapToRegister(registerDto);
        user.setCreatedAt(Instant.now());
        user.setRole(roleRepository.findById(1).get());
        user.setStatus(UserStatusEnum.NOT_ACTIVATED);
        user = userRepository.save(user);

        //Creating user profile
        UserProfileEntity userProfile = new UserProfileEntity();
        userProfile.setUsers(user);
        userProfile.setGender(Gender.Male);
        userProfile.setFullName(registerDto.getFullName());

        this.userProfileRepository.save(userProfile);
    }

    /**
     * Login with social account.
     *
     * @param email    This is email of the user as a social account.
     * @param provider This is the provider of the social account.
     */
    @Override
    public void socialAccountHandler(String email, SocialProvider provider) {

        //Checking if the email is taken by social account
        if (this.userSocialRepository.getSocialAccountUserIdAndProvider(email, provider).isEmpty()) {

            SocialAccountEntity socialAccount = new SocialAccountEntity();
            //Finding user by email
            Optional<UserEntity> user = this.userRepository.findByEmail(email);

            //Checking if user exists or not
            if (user.isPresent()) {
                //Creating social account when user exists
                socialAccount.setUser(user.get());
                socialAccount.setProvider(provider);
                socialAccount.setSocialAccountUserId(email);
                socialAccount.setCreatedAt(Instant.now());
                this.userSocialRepository.save(socialAccount);
            } else {
                //Creating user and social account when both not exist
                UserEntity newUser = new UserEntity();
                newUser.setEmail(email);
                newUser.setCreatedAt(Instant.now());
                newUser.setStatus(UserStatusEnum.NOT_ACTIVATED);
                newUser = this.userRepository.save(newUser);

                socialAccount.setUser(newUser);
                socialAccount.setProvider(provider);
                socialAccount.setSocialAccountUserId(email);
                socialAccount.setCreatedAt(Instant.now());
                this.userSocialRepository.save(socialAccount);
            }
        }
    }

    /**
     * *
     * Sending email with verification code.
     *
     * @param email            This is an email needs to be sent.
     * @param verificationCode This is the verification code of the user.
     * @throws InternalServerErrorException if email sending failed.
     */
    public void sendVerificationEmail(String email, String verificationCode) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("lecongthuong2204@gmail.com");
            helper.setTo(email);
            String subject = "Here's your Verification Code";

            String content = "<p>Hello " + email + "</p>"
                    + "<p>For security reason, you're required to use the following "
                    + "Verification Code:</p>"
                    + "<p><b>" + verificationCode + "</b></p>"
                    + "<br>"
                    + "<p>Note: this verification code just can use once time</p>";
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new InternalServerErrorException("Sent email failed!");
        }
    }

    /**
     * *
     * Generate and set the verification code to the user then send it to email.
     *
     * @param emailDto  This is an email of the user need to be activated.
     * @throws ResourceNotFoundException if could not find the user with that email.
     */
    @Override
    public void sendVerificationCode(EmailDto emailDto) {
        UserEntity user = this.userRepository
                .findByEmail(emailDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + emailDto.getEmail() + " not found!"));

        //Generating a random verification code with 6 characters
        String verificationCode = RandomStringUtils.randomNumeric(6);
        user.setVerificationCode(Integer.valueOf(verificationCode));
        this.userRepository.save(user);
        //Sending verification code to user's email
        sendVerificationEmail(emailDto.getEmail(), verificationCode);
    }

    /**
     * *
     * Confirm verification code.
     *
     * @param verificationCodeDto This is the verification code of the user need to be activated.
     * @return An ID of that user.
     * @throws ResourceNotFoundException if user not found.
     */
    @Override
    public void confirmVerificationCodeAndActiveUser(VerificationCodeDto verificationCodeDto) {
        UserEntity user = this.userRepository
                .findByVerificationCode(verificationCodeDto.getVerificationCode())
                .orElseThrow(() -> new ResourceNotFoundException("Wrong verification code!"));
        //When confirm password successfully then return ID of the user
        user.setStatus(UserStatusEnum.ACTIVATED);
        user.setVerificationCode(null);
        this.userRepository.save(user);
    }

    /**
     * *
     * Confirm verification code for change password.
     *
     * @param verificationCode This is the verification code of the user need to be change password.
     * @return An email of that user.
     * @throws ResourceNotFoundException if user not found.
     */
    @Override
    public String confirmChangePasswordByVerificationCode(VerificationCodeDto verificationCode) {
        UserEntity user = this.userRepository
                .findByVerificationCode(verificationCode.getVerificationCode())
                .orElseThrow(() -> new ResourceNotFoundException("Wrong verification code!"));
        // When confirm password successfully then return email of the user
        return user.getEmail();
    }

    /**
     * *
     * Delete user using ID
     *
     * @param id This is id of user need be deleted.
     * @throws ResourceNotFoundException if user not found.
     */
    @Override
    public void deleteUserById(Long id) {
        UserEntity user = this.userRepository
                .findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("Not found user id")));
        user.setStatus(UserStatusEnum.NOT_ACTIVATED);
        this.userRepository.save(user);
    }

    /**
     * Login with social account.
     *
     * @param socialLoginDto This is data when loggin by social account to save in database.
     */
    @Override
    public void saveUserBySocial(SocialLoginDto socialLoginDto) {
        //Checking if the email is taken by social account
        if (this.userSocialRepository.getSocialAccountUserIdAndProvider(
                socialLoginDto.getEmail(),
                socialLoginDto.getProvider()).isEmpty()) {

            SocialAccountEntity socialAccount = new SocialAccountEntity();
            //Finding user by email
            Optional<UserEntity> user = this.userRepository.findByEmail(socialLoginDto.getEmail());

            //Checking if user exists or not
            if (user.isPresent()) {
                //Creating social account when user exists
                socialAccount.setUser(user.get());
                socialAccount.setProvider(socialLoginDto.getProvider());
                socialAccount.setSocialAccountUserId(socialLoginDto.getEmail());
                socialAccount.setCreatedAt(Instant.now());
                this.userSocialRepository.save(socialAccount);
            } else {
                //Creating user and social account when both not exist
                UserEntity newUser = new UserEntity();
                newUser.setEmail(socialLoginDto.getEmail());
                newUser.setCreatedAt(Instant.now());
                newUser.setStatus(UserStatusEnum.NOT_ACTIVATED);
                newUser = this.userRepository.save(newUser);

                socialAccount.setUser(newUser);
                socialAccount.setProvider(socialLoginDto.getProvider());
                socialAccount.setSocialAccountUserId(socialLoginDto.getEmail());
                socialAccount.setCreatedAt(Instant.now());
                this.userSocialRepository.save(socialAccount);
            }
        }
    }

    /**
     *
     * @param request a refresh token from cookies
     * @return a new access token and refresh token
     */
    @Override
    public HashMap<String, Object> refreshToken(HttpServletRequest request) {
        String refreshToken= this.authRefreshToken.getRefreshTokenFromCookie(request.getCookies());
        HashMap<String,Object> data = new HashMap<>();
        if(refreshToken==null){
            throw new AuthorizeException("Unauthorized");
        }

        if(!this.authRefreshToken.verifyRefreshToken(refreshToken)){
            throw new AuthorizeException("Unauthorized");
        }
        String userName = this.authRefreshToken.extractUserName(refreshToken);
        data.put("accessToken", this.jwtUtils.generateTokenFromUsername(userName));
        data.put("refresh_token",refreshToken);
        return data;
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity user = this.userRepository
                .findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("Not found user id")));

        if (user.getStatus() == UserStatusEnum.NOT_ACTIVATED) {
            throw new NotActivatedExceptionHandler("User hasn't been activated!");
        }

        // Check if the user has been banned.
        if (user.getStatus() == UserStatusEnum.BANNED) {
            throw new AuthorizeException("User has been banned!");
        }
       return this.mapToUserDto(user);
    }


    /**
     * *
     * Change password of the user.
     *
     * @param email           This is an email of the user.
     * @param confirmLoginDto This is a new password of the user.
     * @throws ResourceNotFoundException if the user with that email not found.
     */

    @Override
    public void updatePassword(String email, ConfirmLoginDto confirmLoginDto) {
        UserEntity user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + "not found!"));

        user.setPassword(passwordEncoder.encode(confirmLoginDto.getPassword()));
        user.setVerificationCode(null);
        user.setStatus(UserStatusEnum.ACTIVATED);
        this.userRepository.save(user);
    }

    /**
     * *
     * Convert RegisterDto to UsersEntity.
     *
     * @param registerDto This is all information in the DTO.
     * @return user entity.
     */
    private UserEntity mapToRegister(RegisterDto registerDto) {
        return UserEntity.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();
    }

    private UserDto mapToUserDto(UserEntity user) {
        return UserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }
}
