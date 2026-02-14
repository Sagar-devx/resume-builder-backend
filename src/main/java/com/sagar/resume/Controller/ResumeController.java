package com.sagar.resume.Controller;

import com.sagar.resume.Document.Resume;
import com.sagar.resume.Dto.CreateResumeRequest;
import com.sagar.resume.Service.FileUploadService;
import com.sagar.resume.Service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.sagar.resume.Util.AppConstants.*;

@RestController
@RequestMapping(RESUME)
@Slf4j
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request, Authentication authentication)
    {
        Resume newResume = resumeService.createResume(request,authentication.getPrincipal());

        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);

    }

    @GetMapping()
    public ResponseEntity<?> getUserResumes(Authentication authentication)
    {
        List<Resume> resumes =resumeService.getUserResumes(authentication.getPrincipal());
        return ResponseEntity.ok(resumes);

    }

    @GetMapping(ID)
    public ResponseEntity<?> getResumeById(@PathVariable("id") String id,Authentication authentication)
    {
        Resume resume =resumeService.getResumeById(id,authentication.getPrincipal());

        return ResponseEntity.ok(resume);

    }

    @PutMapping(ID)
    public ResponseEntity<?> updateResume(@PathVariable("id") String id,@RequestBody Resume updatedData,Authentication authentication)
    {
        Resume resume = resumeService.updateResume(id,updatedData,authentication.getPrincipal());
        return ResponseEntity.ok(resume);

    }

    @PutMapping(UPLOAD_IMAGES)
    public ResponseEntity<?> uploadResumeImages(@PathVariable("id")String id,
                                                @RequestPart(value = "thumbnail")MultipartFile thumbnail,
                                                @RequestPart(value = "profileImage",required = false)MultipartFile profileImage,
                                                Authentication authentication)
    {
        Map<String,String> response =fileUploadService.uploadResumeImages(id,authentication.getPrincipal(),thumbnail,profileImage);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping(ID)
    public ResponseEntity<?> deleteResume(@PathVariable("id")String id,Authentication authentication)
    {
        resumeService.deleteResume(id,authentication.getPrincipal());
        return ResponseEntity.ok(Map.of("message","Resume deleted Successfully"));

    }

}
