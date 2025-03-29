package daisy.community_be.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDto {
    private Long userId;
    private String content;
}