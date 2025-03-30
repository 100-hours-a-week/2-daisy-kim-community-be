package daisy.community_be.controller;

import daisy.community_be.dto.response.PostLikeResponseDto;
import daisy.community_be.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/likes")
    public ResponseEntity<?> likePost(
            @PathVariable Long postId,
            @RequestHeader("userId") Long userId) {
        try {
            PostLikeResponseDto response = postLikeService.likePost(postId, userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "like_success", "data", response != null ? response : ""));
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if ("post_not_found".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "post_not_found", "data", ""));
            } else if ("already_liked".equals(msg)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "already_liked", "data", ""));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "invalid_request", "data", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", ""));
        }
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<?> unlikePost(
            @PathVariable Long postId,
            @RequestHeader("userId") Long userId) {
        try {
            postLikeService.unlikePost(postId, userId);
            return ResponseEntity.ok(
                    Map.of("message", "unlike_success", "data", "")
            );
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if ("post_not_found".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "post_not_found", "data", ""));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "invalid_request", "data", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", ""));
        }
    }
}