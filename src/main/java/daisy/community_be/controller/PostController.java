package daisy.community_be.controller;

import daisy.community_be.dto.request.PostCreateRequestDto;
import daisy.community_be.dto.request.PostUpdateRequestDto;
import daisy.community_be.dto.response.PostCreateResponseDto;
import daisy.community_be.dto.response.PostDetailResponseDto;
import daisy.community_be.dto.response.PostListResponseDto;
import daisy.community_be.dto.response.PostUpdateResponseDto;
import daisy.community_be.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getPosts() {
        try {
            List<PostListResponseDto> posts = postService.getAllPosts();

            Map<String, Object> body = new HashMap<>();
            body.put("message", "posts_fetched");
            body.put("data", posts);

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "internal_server_error");
            body.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        try {
            PostDetailResponseDto post = postService.getPostById(postId);

            Map<String, Object> body = new HashMap<>();
            body.put("message", "posts_fetched");
            body.put("data", List.of(post));

            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new HashMap<>();
            if ("post_not_found".equals(e.getMessage())) {
                body.put("message", "post_not_found");
                body.put("data", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
            body.put("message", "invalid_request");
            body.put("data", null);
            return ResponseEntity.badRequest().body(body);
        } catch (Exception e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "internal_server_error");
            body.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto requestDto) {
        try {
            if (requestDto.getTitle() == null || requestDto.getContent() == null) {
                Map<String, Object> body = new HashMap<>();
                body.put("message", "invalid_request");
                body.put("data", null);
                return ResponseEntity.badRequest().body(body);
            }

            PostCreateResponseDto response = postService.createPost(requestDto, requestDto.getUserId());

            Map<String, Object> body = new HashMap<>();
            body.put("message", "post_created");
            body.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new HashMap<>();
            if ("unauthorized".equals(e.getMessage())) {
                body.put("message", "unauthorized");
                body.put("data", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
            }
            body.put("message", "invalid_request");
            body.put("data", null);
            return ResponseEntity.badRequest().body(body);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> body = new HashMap<>();
            body.put("message", "internal_server_error");
            body.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId,
                                        @RequestBody PostUpdateRequestDto requestDto, @RequestHeader("userId") Long userId) {
        try {
            PostUpdateResponseDto response = postService.updatePost(postId, requestDto, userId);

            Map<String, Object> body = new HashMap<>();
            body.put("message", "post_updated");
            body.put("data", response);

            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new HashMap<>();
            if ("post_not_found".equals(e.getMessage())) {
                body.put("message", "post_not_found");
                body.put("data", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
            body.put("message", "invalid_request");
            body.put("data", null);
            return ResponseEntity.badRequest().body(body);
        } catch (SecurityException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "forbidden");
            body.put("data", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> body = new HashMap<>();
            body.put("message", "internal_server_error");
            body.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        @RequestHeader("userId") Long userId) {
        try {
            if (userId == null) {
                Map<String, Object> body = new HashMap<>();
                body.put("message", "invalid_request");
                body.put("data", "");
                return ResponseEntity.badRequest().body(body);
            }

            postService.deletePost(postId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new HashMap<>();
            if ("post_not_found".equals(e.getMessage())) {
                body.put("message", "post_not_found");
                body.put("data", "");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
            body.put("message", "invalid_request");
            body.put("data", "");
            return ResponseEntity.badRequest().body(body);
        } catch (SecurityException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "forbidden");
            body.put("data", "");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> body = new HashMap<>();
            body.put("message", "internal_server_error");
            body.put("data", "");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }
}