package com.sagar.resume.Controller;

import com.sagar.resume.Service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.sagar.resume.Util.AppConstants.TEMPLATES;

@RestController
@RequiredArgsConstructor
@RequestMapping(TEMPLATES)
@Slf4j
public class TemplateController
{
    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<?> getTemplates(Authentication authentication)
    {
       Map<String,Object> response= templateService.getTemplates(authentication.getPrincipal());
       return ResponseEntity.ok(response);

    }
}
