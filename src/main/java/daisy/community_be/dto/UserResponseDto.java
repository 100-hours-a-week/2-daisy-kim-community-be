package daisy.community_be.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String message;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private Long userId;
    }
}
