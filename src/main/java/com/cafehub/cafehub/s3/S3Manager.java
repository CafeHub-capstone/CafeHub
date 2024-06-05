package com.cafehub.cafehub.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Manager {

    private final AmazonS3 amazonS3;

    private final S3Config s3Config;

    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    }


    public void deleteFile(String keyname){
        amazonS3.deleteObject(s3Config.getBucket(),keyname);
    }


    public String generateCafePhotoKeyName() {
        return s3Config.getCafesPath() + '/' + UUID.randomUUID();
    }

    public String generateReviewPhotoKeyName() {
        return s3Config.getReviewsPath() + '/' + UUID.randomUUID();
    }

    public String generateProfilePhotoKeyName() {
        return s3Config.getProfilesPath() + '/' + UUID.randomUUID();
    }
}
