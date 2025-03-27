package daisy.community_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    private String profileImageUrl;
}