package daisy.community_be.dto.request;

import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    private Long userId;
    private String title;
    private String content;
    private String postImage; // optional
}