package escom.ttbackend.controller;

import escom.ttbackend.config.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/new-user/")
@RequiredArgsConstructor
public class NewUserController {
    private final AuthenticationService authenticationService;

//    @PostMapping("register")
//    public ResponseEntity<Object> regi
}
