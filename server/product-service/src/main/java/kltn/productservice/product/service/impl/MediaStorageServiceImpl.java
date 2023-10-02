
package kltn.productservice.product.service.impl;

import kltn.productservice.common.exception.InternalServerErrorException;
import kltn.productservice.product.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
public class MediaStorageServiceImpl implements StorageService {

    DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * *
     * Check image file
     * @param multipartFile This is file need to check
     * @return true or false
     */
    @Override
    public boolean isImageFile(MultipartFile multipartFile) {
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "gif", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    /**
     * *
     * Check video file
     * @param multipartFile This is file need to check
     * @return true or false
     */
    @Override
    public boolean isVideoFile(MultipartFile multipartFile) {
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        return Arrays.asList(new String[]{"mp4", "wmv", "avi", "mov"})
                .contains(fileExtension.trim().toLowerCase());
    }

    /**
     * *
     * Create new file and store in resource
     * @param multipartFile This is file need to store
     * @return Path of file
     */
    @Override
    public String storeFile(MultipartFile multipartFile, Path storageFolder) {
        try {
            if (multipartFile.isEmpty()) {
                throw new IllegalArgumentException("Can't store empty file");
            }

            float fileSize = multipartFile.getSize() / 1_000_000.0f;
            if (fileSize > 60.0f) {
                throw new IllegalArgumentException("File must be < 60Mb");
            }

            // Create file name include current time and a number random 1-100
            LocalDateTime current = LocalDateTime.now();
            StringBuilder filename=new StringBuilder();
            filename.append(current.format(formatter));
            filename.append((int)(Math.random()*100+1));
            filename.append(".");
            filename.append(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));

            // Normalize the save path to the server
            Path destinationFilePath = storageFolder
                    .resolve(Paths.get(filename.toString()))
                    .normalize()
                    .toAbsolutePath();
            if (!destinationFilePath.getParent().equals(storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            // Save file
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // return path to save in database
            return filename.toString();
        }catch(IOException e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
