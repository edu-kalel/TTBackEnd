package escom.ttbackend.controller;

import escom.ttbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/")
public class AdminController {
    private final UserService userService;


    public AdminController(UserService userService) {
        this.userService = userService;
    }

//    @PutMapping("updatePatient")
}
