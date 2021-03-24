package com.epam.esm.controller;

import com.epam.esm.controller.exception.ControllerException;
import com.epam.esm.entity.AuthenticationRequestDto;
import com.epam.esm.entity.User;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.UserService;
import com.epam.esm.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
@Validated
public class AuthenticationControllerV1 {
    private static final String USER_ENTITY_CODE = "03";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";
    private static final String ROLE = "role";
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userService.findByEmail(email).orElseThrow(() ->
                    new UsernameNotFoundException("User doesn't exist"));
            String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put(EMAIL, email);
            response.put(ROLE, user.getRole());
            response.put(TOKEN, token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }
}
