package daisy.community_be.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private String postImage;
}