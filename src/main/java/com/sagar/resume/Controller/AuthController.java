package com.sagar.resume.Controller;
import com.sagar.resume.Document.User;
import com.sagar.resume.Dto.AuthResponse;
import com.sagar.resume.Dto.LoginRequest;
import com.sagar.resume.Dto.RegisterRequest;
import com.sagar.resume.Service.AuthService;
import com.sagar.resume.Service.FileUploadService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

import static com.sagar.resume.Util.AppConstants.*;

@RestController
@Slf4j
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private FileUploadService fileUploadService;

    public AuthController(AuthService authService,FileUploadService fileUploadService) {
        this.authService = authService;
        this.fileUploadService=fileUploadService;
    }
    @PostMapping(REGISTER)
    public ResponseEntity<?> register(@Valid  @RequestBody RegisterRequest request)
    {
        log.info("Inside AuthController - register(): {}",request);
        AuthResponse response = authService.register(request);
        log.info("Response from service() {}",response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam("token")String token)
    {
        log.info("Inside AuthController - verifyEmail() {}",token);
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Email verified Successfully"));

    }

    @PostMapping(UPLOAD_IMAGE)
    public ResponseEntity<?> uploadImage(@RequestPart("image")MultipartFile file)
    {
        log.info("Inside AuthController - UploadImage() ");
      Map<String,String> response = fileUploadService.uploadSingleImage(file);
      return  ResponseEntity.ok(response);
    }

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request)
    {
       AuthResponse response= authService.login(request);
       return ResponseEntity.ok(response);

    }

    @PostMapping(RESEND_VERIFICATION)
    public ResponseEntity<?> resendVerification(@RequestBody Map<String,String> body)
    {
        String email =body.get("email");

        if(Objects.isNull(email))
        {
          return ResponseEntity.badRequest().body(Map.of("message","Email is required"));
        }
        authService.resendVerification(email);

        return ResponseEntity.ok(Map.of("success",true,"message","Verification email sent"));
    }

    @GetMapping(PROFILE)
    public ResponseEntity<?> getProfile(Authentication authentication)
    {
        //Step 1:Get the principal object

      Object principalObject =authentication.getPrincipal();

        //Step 2:Call the service method

        AuthResponse currentProfile =authService.getProfile(principalObject);

        //Step 3:return the response

        return ResponseEntity.ok(currentProfile);


    }


}
