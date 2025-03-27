package daisy.community_be.controller;

import daisy.community_be.dto.request.LoginRequest;
import daisy.community_be.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sessions")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            // 1) 로그인 시도
            Long userId = authService.login(request.getEmail(), request.getPassword());

            // 2) 로그인 성공 시
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "login_success");
            response.put("data", data);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 400 Error (이메일로 유저를 찾지 못한 경우)
            Map<String, Object> response = new HashMap<>();
            response.put("message", "invalid_request");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        } catch (SecurityException e) {
            // 401 Error (비밀번호 불일치)
            Map<String, Object> response = new HashMap<>();
            response.put("message", "unauthorized");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            // 500 Error (그 외 예상치 못한 예외)
            Map<String, Object> response = new HashMap<>();
            response.put("message", "internal_server_error");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}