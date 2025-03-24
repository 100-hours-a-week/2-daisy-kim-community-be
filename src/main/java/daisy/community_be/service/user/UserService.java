package daisy.community_be.service.user;

import daisy.community_be.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import daisy.community_be.domain.user.User;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
