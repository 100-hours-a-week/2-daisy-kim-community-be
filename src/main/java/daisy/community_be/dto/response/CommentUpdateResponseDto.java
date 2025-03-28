package daisy.community_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentUpdateResponseDto {
    private Long commentId;
    private String content;
    private LocalDateTime updatedAt;
}