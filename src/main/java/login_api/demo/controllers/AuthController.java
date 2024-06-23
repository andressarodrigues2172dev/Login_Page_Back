package login_api.demo.controllers;


import login_api.demo.domain.user.User;
import login_api.demo.dto.LoginRequestDTO;
import login_api.demo.dto.RegisterRequestDTO;
import login_api.demo.dto.ResponseDTO;
import login_api.demo.infra.security.TokenService;
import login_api.demo.repositoreis.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {

        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.password(),user.getPassword())) {
            String token = this.tokenService.genereteToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = this.repository.findByEmail(body.email());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.repository.save(newUser);

            String token = this.tokenService.genereteToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }


        return ResponseEntity.badRequest().build();
}


}
