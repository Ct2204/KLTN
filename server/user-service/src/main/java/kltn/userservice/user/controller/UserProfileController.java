package kltn.userservice.user.controller;


import jakarta.validation.Valid;
import kltn.userservice.common.dto.ResponseDto;
import kltn.userservice.user.dto.ResponseDataDto;
import kltn.userservice.user.dto.UpdatingUserProfileDto;
import kltn.userservice.user.service.impl.UserProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users/profile")
public class UserProfileController {

    @Autowired
    UserProfileServiceImpl userProfileService;

    /**
     * *
     * This method handles get all users profile request.
     *
     * @return A list of users profile.
     */
    @GetMapping("")
    public ResponseEntity<ResponseDataDto> getAllUsersProfile() {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved all user profiles!");
        responseDataDto.setData(this.userProfileService.getAllUsersProfile());
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles get a user profile by ID request.
     *
     * @param id This is an ID of the user is also be an ID of the user profile.
     *
     * @return The profile of the user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataDto> getUserProfileById(@PathVariable("id") Long id) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved user profiles by user ID!");
        responseDataDto.setData(this.userProfileService.getUserProfileById(id));
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles get a user profile by name request.
     *
     * @param fullName This is the name of those users profile.
     *
     * @return The list users profile.
     */
    @GetMapping("/by-name")
    public ResponseEntity<ResponseDataDto> getAllUsersProfileByName(@RequestParam("name") String fullName) {
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved all user profiles by user name!");
        responseDataDto.setData(this.userProfileService.getUsersProfileByName(fullName));
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles get a user picture profile by user ID request.
     *
     * @param id This is an ID of the user is also be an ID of the user profile.
     *
     * @return The picture of the user.
     */
    @GetMapping("/get-picture/{id}")
    public ResponseEntity<ResponseDataDto> getProfilePictureByUserId(@PathVariable("id") Long id) {
        HashMap<String, String> picturePath = new HashMap<>();
        picturePath.put("path", this.userProfileService.getProfilePictureByUserId(id));
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successfully retrieved all profile picture by user ID!");
        responseDataDto.setData(picturePath);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles update a user profile request.
     *
     * @param id                 This is an ID of the user is also be an ID of the user profile (pathvariable).
     * @param userProfileRequest This is all information to update a user profile (requestbody).
     *
     * @return A message 'Updated successfully!'.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto> updateUserProfile(@PathVariable("id") Long id,
                                                         @RequestBody @Valid UpdatingUserProfileDto userProfileRequest) {
        this.userProfileService.updateUserProfile(id, userProfileRequest);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("update user profile successful!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles upload a picture profile of the user.
     *
     * @param id   This is an ID of the user is also be an ID of the user profile.
     * @param file This is the picture from the user request.
     *
     * @return A message 'Updated successfully!'.
     */
    @PostMapping("/upload-picture/{id}")
    public ResponseEntity<ResponseDto> uploadProfilePicture(@PathVariable("id") Long id,
                                                           @RequestBody MultipartFile file) {
        this.userProfileService.uploadProfilePicture(id, file);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("upload profile picture successful!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
