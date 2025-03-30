package daisy.community_be.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDto {
    private String title;
    private String content;
    @JsonProperty("postImage")
    private String postImage;
}