package com.sagar.resume.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateResumeRequest
{
    @NotBlank(message = "Title is required")
    private String title;


}
