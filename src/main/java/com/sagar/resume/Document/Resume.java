package com.sagar.resume.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;a

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "resumes")
public class Resume {

    @Id
    @JsonProperty("_id")
    private String id;
    private String userId;
    private String title;
    private String thumbnailLink;
    private Template template;
    private ProfileInfo profileInfo;
    private ContactInfo contactInfo;
    private List<WorkExperience> workExperiences;
    private List<Education> educations;
    private List<Skill> skills;
    private List<Project> projects;
    private List<Certification> certifications;
    private List<Language> languages;
    private List<String> interests;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class Template{
        private String theme;
        private List<String> colorPalette;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ProfileInfo{

        private String profilePreviewUrl;
        private String fullName;
        private String summary;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ContactInfo{

        private String email;
        private String phone;
        private String location;
        private String linkedin;
        private String gitHub;
        private String webSite;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class WorkExperience{

        private String company;
        private String role;
        private String startDate;
        private String endDate;
        private String description;
    }
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class Education{

        private String degree;
        private String institution;
        private String startDate;
        private String endDate;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class Skill{

        private String name;
        private Integer progress;

    }
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class Project{

        private String title;
        private String description;
        private String gitHub;
        private String liveDemo;

    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class Certification{

        private String title;
        private String issuer;
        private String year;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class Language{

        private String name;
        private Integer progess;
    }


}
