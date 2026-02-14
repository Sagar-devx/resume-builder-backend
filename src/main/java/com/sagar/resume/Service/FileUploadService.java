package com.sagar.resume.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sagar.resume.Document.Resume;
import com.sagar.resume.Dto.AuthResponse;
import com.sagar.resume.Repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private static final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepository resumeRepository;

    public Map<String, String> uploadSingleImage(MultipartFile file) {
        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );

            log.info("Uploaded image: {}", result.get("secure_url"));

            return Map.of("imageUrl", result.get("secure_url").toString());

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    public Map<String, String> uploadResumeImages(
            String id,
            Object principal,
            MultipartFile thumbnail,
            MultipartFile profileImage
    ) {

        AuthResponse response = authService.getProfile(principal);
        Resume resume = resumeRepository
                .findByUserIdAndId(response.getId(), id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Map<String, String> returnValue = new HashMap<>();

        if (Objects.nonNull(thumbnail)) {
            Map<String, String> uploadResult = uploadSingleImage(thumbnail);
            String thumbnailUrl = uploadResult.get("imageUrl");

            returnValue.put("thumbnailLink", thumbnailUrl);
            resume.setThumbnailLink(thumbnailUrl);
        }


        if (Objects.nonNull(profileImage)) {
            Map<String, String> uploadResult = uploadSingleImage(profileImage);
            String profileUrl = uploadResult.get("imageUrl");

            returnValue.put("profilePreviewUrl", profileUrl);

            if (Objects.isNull(resume.getProfileInfo())) {
                resume.setProfileInfo(new Resume.ProfileInfo());
            }

            resume.getProfileInfo().setProfilePreviewUrl(profileUrl);
        }

        resumeRepository.save(resume);

        returnValue.put("message","Images uploaded successfully");

        return returnValue;
    }
}

