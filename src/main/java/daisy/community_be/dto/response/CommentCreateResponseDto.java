package daisy.community_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentCreateResponseDto {
    private Long commentId;
    private String content;
    private AuthorDto author;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class AuthorDto {
        private String nickname;
        private String profileImage;
    }
}