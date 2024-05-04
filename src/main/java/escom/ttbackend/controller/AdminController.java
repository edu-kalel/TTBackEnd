package escom.ttbackend.controller;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.service.implementation.AdminService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }
    /*@GetMapping("/patients")
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        List<UserDTO> patients = adminService.get
    }*/
}
