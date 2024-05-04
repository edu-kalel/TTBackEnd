package escom.ttbackend.controller;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.PostDTO;
import escom.ttbackend.service.implementation.PatientService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {
    private final UserService userService;
    private final PatientService patientService;

    public PatientController(UserService userService, PatientService patientService) {
        this.userService = userService;
        this.patientService = patientService;
    }

    @PostMapping("/post")
    public ResponseEntity<PostDTO> newPost(@RequestBody String content){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(patientService.newPost(content, user.getEmail()), HttpStatus.OK);
    }
}
