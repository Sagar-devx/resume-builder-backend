package com.sagar.resume.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    @Size(min = 2,max = 30,message = "Name must be between 2 and 30 characters ")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6,max = 15,message = "Password must be between 6 and 15 characters")
    private String password;

    private String profileImageUrl;

    public @Email(message = "Email should be valid") @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be valid") @NotBlank(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Name is required") @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters ") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is required") @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters ") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Password is required") @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters") String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
