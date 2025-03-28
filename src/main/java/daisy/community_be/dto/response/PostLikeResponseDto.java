package daisy.community_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostLikeResponseDto {
    private Long likeId;
    private Long postId;
    private LocalDateTime likedAt;
}