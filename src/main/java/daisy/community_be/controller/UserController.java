package daisy.community_be.controller;

import daisy.community_be.dto.request.UserRequestDto;
import daisy.community_be.dto.request.UserUpdateRequestDto;
import daisy.community_be.dto.response.UserResponseDto;
import daisy.community_be.service.UserEditService;
import daisy.community_be.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserEditService userEditService;

    public UserController(UserService userService, UserEditService userEditService) {
        this.userService = userService;
        this.userEditService = userEditService;
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto requestDto) {
        try {
            userEditService.updateUser(userId, requestDto);
            return ResponseEntity.ok(Map.of("message", "edit_success"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "invalid_request", "data", null));
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(Map.of("message", "unauthorized", "data", null));
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(409).body(Map.of("message", "nickname_already_taken", "data", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "internal_server_error", "data", null));
        }
    }
}
