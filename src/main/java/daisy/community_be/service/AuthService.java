package daisy.community_be.service;

import daisy.community_be.domain.User;
import daisy.community_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public Long login(String email, String password) {
        // 1) 이메일로 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));

        // 2) 비밀번호 일치 여부 확인
        if (password == null || !password.equals(user.getPassword())) {
            throw new SecurityException("Wrong password");
        }

        // 3) 성공 시 유저 ID 반환
        return user.getId();
    }
}