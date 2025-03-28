package daisy.community_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostUpdateResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String profileImageUrl;
    private LocalDateTime updatedAt;
}