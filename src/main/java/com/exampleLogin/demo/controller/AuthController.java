package com.exampleLogin.demo.controller;

import com.exampleLogin.demo.UserDto;
import com.exampleLogin.demo.model.User;
import com.exampleLogin.demo.service.UserService;
import com.exampleLogin.demo.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        UserDetails userDetails = userService.loadUserByUsername(userDto.getUsername()); // Cambié aquí a UserDetails
        if (userDetails != null && passwordEncoder.matches(userDto.getPassword(), userDetails.getPassword())) {
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }


        @GetMapping("/resource")
        @PreAuthorize("hasRole('USER')") // Asegúrate de tener roles configurados si usas @PreAuthorize
        public ResponseEntity<String> getProtectedResource() {
            return ResponseEntity.ok("Este es un recurso protegido!");
        }
    }

