package daisy.community_be.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequestDto {
    private Long userId;
    private String content;
}