package escom.ttbackend.controller;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.PatientRegistrationBySecretaryDTO;
import escom.ttbackend.presentation.dto.ReassignationRequest;
import escom.ttbackend.presentation.dto.StaffRegistrationDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.service.implementation.AdminService;
import escom.ttbackend.service.implementation.NutriService;
import escom.ttbackend.service.implementation.SecretaryService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;
    private final SecretaryService secretaryService; //Only for development purposes, can be deleted when dev endpoints are deleted

    public AdminController(UserService userService, AdminService adminService, SecretaryService nutriService) {
        this.userService = userService;
        this.adminService = adminService;
        this.secretaryService = nutriService;
    }

    @DeleteMapping("/user/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email){
        if (!userService.existsById(email))
            throw new UsernameNotFoundException("User does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        if (userService.isFromClinic(admin.getClinic(), email)){
//            userService.deletePatient(email);
            adminService.deleteUser(email);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    @PostMapping("/new-staff-user")
    public ResponseEntity<UserDTO> registerNewStaff(@RequestBody StaffRegistrationDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        return new ResponseEntity<>(adminService.registerNewUser(registerRequest,admin.getClinic()), HttpStatus.CREATED);
    }

    @PostMapping("/development/list/new-staff-user")
    public ResponseEntity<String> registerNewStaffBatch(@RequestBody Set<StaffRegistrationDTO> registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        for (StaffRegistrationDTO staffRegistrationDTO : registerRequest){
            adminService.registerNewUser(staffRegistrationDTO,admin.getClinic());
        }
        return new ResponseEntity<>("idk man, maybe it's ok", HttpStatus.CREATED);
    }

    @PostMapping("/development/list/new-patient")
    public ResponseEntity<String> registerNewPatientsBatch(@RequestBody Set<PatientRegistrationBySecretaryDTO> registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        for (PatientRegistrationBySecretaryDTO patientRegistrationDTO : registerRequest){
            if (userService.validateParentEmailForUser(patientRegistrationDTO.getParent_email(), secretary.getEmail()))
                secretaryService.registerNewPatient(patientRegistrationDTO);
            else throw new BadCredentialsException("No permissions over this user");
        }
        return new ResponseEntity<>("idk man, maybe it's ok", HttpStatus.CREATED);
    }

    @PutMapping("/update-staff-user")
    public ResponseEntity<UserDTO> updateStaff(@RequestBody StaffRegistrationDTO updateRequest){
        if(!userService.existsById(updateRequest.getEmail()))
            throw new UsernameNotFoundException("User does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        if (!userService.isFromClinic(admin.getClinic(), updateRequest.getEmail())){
            throw new BadCredentialsException("This user is not part of your clinic");
        }
        return new ResponseEntity<>(adminService.updateUser(updateRequest), HttpStatus.OK);
    }

    @PutMapping("/reassign-parent-to-user")
    public ResponseEntity<UserDTO> reassignParent(@RequestBody ReassignationRequest updateRequest){
        if(!userService.existsById(updateRequest.getChild_email()))
            throw new UsernameNotFoundException("Child User does not exist");
        if(!userService.existsById(updateRequest.getParent_email()))
            throw new UsernameNotFoundException("Parent User does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        if (!userService.isFromClinic(admin.getClinic(), updateRequest.getChild_email()))
            throw new BadCredentialsException("Child User is not part of your clinic");
        if (!userService.isFromClinic(admin.getClinic(), updateRequest.getParent_email()))
            throw new BadCredentialsException("Parent User is not part of your clinic");
        return new ResponseEntity<>(adminService.reassignParent(updateRequest), HttpStatus.OK);
    }

    @GetMapping("/staff")
    public ResponseEntity<List<UserDTO>> getAllStaff(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        List<UserDTO> staff = adminService.getStaffByClinic(admin.getClinic());
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        List<UserDTO> staff = adminService.getUsersByClinic(admin.getClinic());
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }
}
