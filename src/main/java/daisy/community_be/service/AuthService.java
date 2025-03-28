package daisy.community_be.service;

import daisy.community_be.domain.User;
import daisy.community_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long login(String email, String rawPassword) {
        // 1) 이메일로 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("user_not_found"));

        // 2) 비밀번호 일치 여부 확인 (BCrypt 비교)
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new SecurityException("unauthorized");
        }

        // 3) 로그인 성공 시 유저 ID 반환
        return user.getId();
    }
}