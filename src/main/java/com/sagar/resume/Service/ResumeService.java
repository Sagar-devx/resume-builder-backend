package com.sagar.resume.Service;

import com.sagar.resume.Document.Resume;
import com.sagar.resume.Document.User;
import com.sagar.resume.Dto.AuthResponse;
import com.sagar.resume.Dto.CreateResumeRequest;
import com.sagar.resume.Repository.ResumeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AuthService authService;

    public ResumeService(ResumeRepository resumeRepository,AuthService authService)
    {
        this.resumeRepository=resumeRepository;
        this.authService=authService;
    }

    public Resume createResume(CreateResumeRequest request, Object principalObject)
    {
        //Create Resume Object
       Resume newresume = new Resume();

       //Get the current profile
       AuthResponse response = authService.getProfile(principalObject);

       //update the resume object
        newresume.setUserId(response.getId());
        newresume.setTitle(request.getTitle());

        //Set default data for resume
        setDefaultResumeData(newresume);

        return resumeRepository.save(newresume);

    }

    public void setDefaultResumeData(Resume newResume)
    {
        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducations(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());

    }
    public List<Resume> getUserResumes(Object principal)
    {
        AuthResponse response =authService.getProfile(principal);

        List<Resume> resumes =resumeRepository.findByUserIdOrderByUpdatedAtDesc(response.getId());

        return  resumes;

    }

    public Resume getResumeById(String id,Object principal)
    {
        AuthResponse response =authService.getProfile(principal);

         Resume existingResume =resumeRepository.findByUserIdAndId(response.getId(),id).orElseThrow(()->new RuntimeException("Resume not found"));
         return existingResume;

    }
    public Resume updateResume(String id, Resume updatedData, Object principal)
    {
        AuthResponse response = authService.getProfile(principal);

        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        existingResume.setTitle(updatedData.getTitle());
        existingResume.setThumbnailLink(updatedData.getThumbnailLink());
        existingResume.setTemplate(updatedData.getTemplate());

        existingResume.setProfileInfo(updatedData.getProfileInfo());
        existingResume.setContactInfo(updatedData.getContactInfo());

        existingResume.setWorkExperiences(updatedData.getWorkExperiences());
        existingResume.setEducations(updatedData.getEducations());
        existingResume.setSkills(updatedData.getSkills());
        existingResume.setProjects(updatedData.getProjects());
        existingResume.setCertifications(updatedData.getCertifications());
        existingResume.setLanguages(updatedData.getLanguages());
        existingResume.setInterests(updatedData.getInterests());

        return resumeRepository.save(existingResume);
    }

    public void deleteResume(String id,Object principal)
    {
        AuthResponse response = authService.getProfile(principal);
        Resume resume = resumeRepository.findByUserIdAndId(response.getId(),id).orElseThrow(()->new RuntimeException("Resume not found"));
        resumeRepository.delete(resume);
    }

}
