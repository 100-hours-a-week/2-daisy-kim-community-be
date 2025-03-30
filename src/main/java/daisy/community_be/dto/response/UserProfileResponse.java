package daisy.community_be.dto.response;

import daisy.community_be.domain.User;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImageUrl;

    public UserProfileResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}