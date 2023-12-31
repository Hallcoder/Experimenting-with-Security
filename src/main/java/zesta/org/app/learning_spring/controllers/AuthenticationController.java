package zesta.org.app.learning_spring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zesta.org.app.learning_spring.dao.UserDao;
import zesta.org.app.learning_spring.dto.AuthenticationRequest;
import zesta.org.app.learning_spring.security.JwtUtils;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtService;
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest req){
        System.out.println("logging inn..." + req.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword())
        );
        System.out.println("Authentication done");
        final UserDetails user = userDao.findUserByEmail(req.getEmail());
        if(user != null){
            return ResponseEntity.ok(jwtService.generateToken(user));
        }
        return ResponseEntity.status(400).body("Some error occurred");
    }
}
