package daisy.community_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long postId;
    private String title;
    private String content;
    private AuthorDto author;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class AuthorDto {
        private String nickname;
        private String profileImage;
    }
}