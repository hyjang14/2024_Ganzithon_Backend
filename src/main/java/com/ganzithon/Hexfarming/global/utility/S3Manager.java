package com.ganzithon.Hexfarming.global.utility;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ganzithon.Hexfarming.domain.user.util.UUIDManager;
import com.ganzithon.Hexfarming.global.enumeration.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Manager {
    private static final List<String> VALID_IMAGE_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "bmp", "gif", "webp");
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.CANNOT_CONVERT_FILE.getMessage()));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        targetFile.delete();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        String originalExtension = getFileExtension(file.getOriginalFilename());
        validateFileIsImage(originalExtension);
        String randomName = UUIDManager.generateUniqueString() + "." + originalExtension;

        File convertFile = new File(randomName);
        System.out.println(convertFile.createNewFile());
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
            fileOutputStream.write(file.getBytes());
        }

        return Optional.of(convertFile);
    }

    private void validateFileIsImage(String fileExtension) {
        if (VALID_IMAGE_EXTENSIONS.contains(fileExtension)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.ONLY_IMAGE_FILE_AVAILABLE.getMessage());
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    public void delete(String fileName) {
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.CANNOT_DELETE_FILE.getMessage());
        }
    }
}
