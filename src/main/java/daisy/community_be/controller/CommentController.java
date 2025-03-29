package daisy.community_be.controller;

import daisy.community_be.dto.request.CommentCreateRequestDto;
import daisy.community_be.dto.request.CommentUpdateRequestDto;
import daisy.community_be.dto.response.CommentCreateResponseDto;
import daisy.community_be.dto.response.CommentListResponseDto;
import daisy.community_be.dto.response.CommentUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import daisy.community_be.service.CommentService;

import java.util.HashMap;
import java.util.List;
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

            Long userId = requestDto.getUserId();

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

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        try {
            List<CommentListResponseDto> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(Map.of(
                    "message", "comments_fetched",
                    "data", comments
            ));
        } catch (IllegalArgumentException e) {
            if ("post_not_found".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "post_not_found", "data", null));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "invalid_request", "data", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", null));
        }
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId,
                                           @PathVariable Long commentId,
                                           @RequestBody CommentUpdateRequestDto requestDto) {

        if (requestDto.getContent() == null || requestDto.getContent().isBlank()) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("message", "invalid_request");
            errorBody.put("data", null);
            return ResponseEntity.badRequest().body(errorBody);
        }

        try {
            Long userId = requestDto.getUserId();
            CommentUpdateResponseDto response = commentService.updateComment(postId, commentId, requestDto, userId);

            if (response == null) {
                Map<String, Object> errorBody = new HashMap<>();
                errorBody.put("message", "null_response_from_service");
                errorBody.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
            }

            Map<String, Object> body = new HashMap<>();
            body.put("message", "comment_updated");
            body.put("data", response);
            return ResponseEntity.ok(body);

        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            HttpStatus status = "comment_not_found".equals(msg) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("message", msg);
            errorBody.put("data", null);
            return ResponseEntity.status(status).body(errorBody);
        } catch (SecurityException e) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("message", "forbidden");
            errorBody.put("data", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("message", "internal_server_error");
            errorBody.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId,
                                           @PathVariable Long commentId, @RequestBody CommentUpdateRequestDto requestDto) {
        try {
            Long userId = requestDto.getUserId();

            commentService.deleteComment(postId, commentId, userId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("message", "comment_deleted"));
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if ("comment_not_found".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "comment_not_found", "data", null));
            } else if ("invalid_request".equals(msg)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "invalid_request", "data", null));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "invalid_request", "data", null));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "forbidden", "data", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", null));
        }
    }
}