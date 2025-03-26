package daisy.community_be.service;

import daisy.community_be.domain.User;
import daisy.community_be.dto.UserRequestDto;
import daisy.community_be.dto.UserResponseDto;
import daisy.community_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRequestDto request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .nickname(request.getNickname())
                .build();

        User saved = userRepository.save(user);

        return new UserResponseDto("register_success", saved.getId());
    }
}