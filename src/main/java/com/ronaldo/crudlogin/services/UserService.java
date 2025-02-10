package com.ronaldo.crudlogin.services;

import com.ronaldo.crudlogin.dao.UserRepository;
import com.ronaldo.crudlogin.dto.LoginRequest;
import com.ronaldo.crudlogin.dto.LoginResponse;
import com.ronaldo.crudlogin.dto.UserRequest;
import com.ronaldo.crudlogin.dto.UserResponse;
import com.ronaldo.crudlogin.models.User;
import com.ronaldo.crudlogin.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public LoginResponse login (LoginRequest loginRequest) throws Exception {

        User user = userRepository.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new Exception("Credenciales incorrectas"));

        if (this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(this.jwtUtil.generateToken(user.getRole()), "usario logueado");
        }
        else {
            throw new Exception("Credenciales incorrectas");
        }
    }

    public UserResponse create (UserRequest userRequest) throws Exception {
        this.userRepository.findUserByUsername(userRequest.getUsername()).ifPresent(user -> {
            throw new RuntimeException("User name ya registrado");
        });
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setStatus(true);

        this.userRepository.save(user);

        return new UserResponse(user.getUsername(),
                user.getRole(),
                "usuario creado");
    }

}
