package daisy.community_be.controller;

import daisy.community_be.dto.UserRequestDto;
import daisy.community_be.dto.UserResponseDto;
import daisy.community_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRequestDto request) {
        try {
            UserResponseDto response = userService.register(request);
            return ResponseEntity.status(201).body(response);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UserResponseDto("invalid_request", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new UserResponseDto("internal_server_error", null));
        }
    }
}
