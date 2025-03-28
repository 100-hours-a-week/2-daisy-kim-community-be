package daisy.community_be.controller;

import daisy.community_be.dto.request.CommentCreateRequestDto;
import daisy.community_be.dto.response.CommentCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import daisy.community_be.service.CommentService;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                           @RequestBody CommentCreateRequestDto requestDto) {
        try {
            if (requestDto.getContent() == null || requestDto.getContent().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "invalid_request", "data", null));
            }

            Long userId = 1L; // 인증 연동 전 임시 사용자 ID

            CommentCreateResponseDto response = commentService.createComment(postId, requestDto, userId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "comment_created", "data", response));
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if ("post_not_found".equals(message)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "post_not_found", "data", null));
            } else if ("unauthorized".equals(message)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "unauthorized", "data", null));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "invalid_request", "data", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", null));
        }
    }
}