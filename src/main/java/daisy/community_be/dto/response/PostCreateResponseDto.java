package daisy.community_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostCreateResponseDto {
    private Long postId;
    private String title;
    private LocalDateTime createdAt;
}