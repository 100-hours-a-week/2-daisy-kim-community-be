package daisy.community_be.controller;

import daisy.community_be.domain.User;
import daisy.community_be.dto.request.UserRequestDto;
import daisy.community_be.dto.request.UserUpdateRequestDto;
import daisy.community_be.dto.response.UserResponseDto;
import daisy.community_be.dto.response.UserProfileResponse;
import daisy.community_be.service.UserEditService;
import daisy.community_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserEditService userEditService;

    /**
     * 회원가입
     */
    @PostMapping("")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto requestDto
    ) {
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

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "user_not_found", "data", null));
            }
            UserProfileResponse response = new UserProfileResponse(user);
            return ResponseEntity.ok(Map.of("message", "profile_fetched", "data", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", null));
        }
    }
}