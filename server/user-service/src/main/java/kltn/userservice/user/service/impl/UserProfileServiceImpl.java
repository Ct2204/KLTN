package kltn.userservice.user.service.impl;


import kltn.userservice.common.entity.UserProfileEntity;
import kltn.userservice.common.exception.InternalServerErrorException;
import kltn.userservice.common.exception.ResourceNotFoundException;
import kltn.userservice.user.dto.UpdatingUserProfileDto;
import kltn.userservice.user.dto.UserProfileDto;
import kltn.userservice.user.repository.UserProfileRepository;
import kltn.userservice.user.repository.UserRepository;
import kltn.userservice.user.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private static final String IMAGE_PROFILE_PATH = "/images/profile";

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserRepository usersRepository;

    @Value("${image.dir.profile}")
    private String dirLocation;

    @Value("${kltn.domain}")
    private String domain;

    /**
     * Get all users profile.
     *
     * @return A list of users profile.
     */
    @Override
    public List<UserProfileDto> getAllUsersProfile() {
        return this.userProfileRepository.getAllUsersProfile();
    }

    /**
     * Get a user profile by ID.
     *
     * @param id This is an ID of the user is also be an ID of the user profile.
     * @return The profile of the user.
     * @throws ResourceNotFoundException if the ID of the user profile  not found.
     */
    @Override
    public UserProfileDto getUserProfileById(Long id) {

        // Checking if the user exists or not
        if (this.usersRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        // Checking if the user profile exists or not
        if (this.userProfileRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User profile with id " + id + " not found");
        }

        return this.userProfileRepository.getAllUserProfileById(id);
    }

    /**
     * Get a user profile by name.
     *
     * @param fullName This is the name of those users profile.
     * @return The list users profile.
     */
    @Override
    public List<UserProfileDto> getUsersProfileByName(String fullName) {

        return this.userProfileRepository.getAllUsersByName(fullName);
    }

    /**
     * Update a user profile.
     *
     * @param id                 This is an ID of the user is also be an ID of the user profile.
     * @param userProfileRequest This is all information to update a user profile.
     * @throws ResourceNotFoundException if the ID of the user not found.
     */
    @Override
    public void updateUserProfile(Long id, UpdatingUserProfileDto userProfileRequest) {

        // Checking if the user exists or not
        if (this.usersRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        // Checking if the user profile exists or not
        UserProfileEntity userProfile =
                this.userProfileRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User profile with id " + id + " not found"));

        userProfile.setFullName(userProfileRequest.getFullName());
        userProfile.setGender(userProfileRequest.getGender());
        userProfile.setBirthday(userProfileRequest.getBirthdayInDate());
        userProfile.setUpdatedAt(Instant.now());

        this.userProfileRepository.save(userProfile);
    }

    /**
     * Upload a user picture profile.
     *
     * @param id   This is an ID of the user is also be an ID of the user profile.
     * @param file This is the picture from the user request.
     * @throws ResourceNotFoundException    if the ID of the user or the file not found.
     * @throws InternalServerErrorException if file upload fail.
     */
    @Override
    public void uploadProfilePicture(Long id, MultipartFile file) {

        String decode = sha256(String.valueOf(System.currentTimeMillis()));

        //Checking if the user exists or not
        if (this.usersRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        // Checking if the user profile exists or not
        UserProfileEntity userProfile =
                this.userProfileRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User profile with id " + id + " not found"));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file to upload!");
        }

        try {
            // Generate a unique file name to avoid conflicts
            int lastDotIndex = file.getOriginalFilename().toString().lastIndexOf(".");
            String fileNameDecoded = decode + "." + file
                    .getOriginalFilename()
                    .toString()
                    .substring(lastDotIndex + 1);
            // Get the resource folder path
            Path resourcePath = Paths.get(dirLocation);

            // Formatting the path
            Path destinationFilePath = resourcePath
                    .resolve(Paths.get(fileNameDecoded))
                    .normalize();

            // Save the file to the specified path
            Files.copy(file.getInputStream(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            // Setting the path of picture profile as a relative path
            userProfile.setProfilePicture(domain + IMAGE_PROFILE_PATH + "/" + fileNameDecoded);
            this.userProfileRepository.save(userProfile);
        } catch (IOException e) {
            // Handle file upload failure
            throw new InternalServerErrorException("Failed to upload the file!");
        }
    }

    public static String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get a user picture profile by user ID.
     *
     * @param id This is an ID of the user is also be an ID of the user profile.
     * @return The picture of the user.
     * @throws ResourceNotFoundException if the ID of the user profile or the picture not found.
     */
    @Override
    public String getProfilePictureByUserId(Long id) {

        // Checking if the user exists or not
        if (this.usersRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        // Checking if the user profile exists or not
        UserProfileEntity userProfile =
                this.userProfileRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User profile with id " + id + " not found"));

        if (userProfile.getProfilePicture() == null) {
            return null;
        }

        return userProfile.getProfilePicture();
    }
}
