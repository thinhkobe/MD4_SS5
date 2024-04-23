package ra.scurity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.scurity.model.dto.request.UserLogin;
import ra.scurity.model.dto.request.UserRegister;
import ra.scurity.service.IUserservice;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserservice iUserservice;
    @PostMapping("/register")
    public ResponseEntity<?> handleRegister(@RequestBody @Valid UserRegister userRegister){
        iUserservice.register(userRegister);
        return new ResponseEntity<>("Register successfully", HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody @Valid UserLogin userLogin) {
        return new ResponseEntity<>(iUserservice.login(userLogin), HttpStatus.OK);
    }


}
