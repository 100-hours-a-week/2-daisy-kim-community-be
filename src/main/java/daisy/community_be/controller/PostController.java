package daisy.community_be.controller;

import daisy.community_be.dto.request.PostCreateRequestDto;
import daisy.community_be.dto.request.PostListResponseDto;
import daisy.community_be.dto.response.PostCreateResponseDto;
import daisy.community_be.dto.response.PostDetailResponseDto;
import daisy.community_be.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // 게시글 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getPosts() {
        try {
            List<PostListResponseDto> posts = postService.getAllPosts();
            return ResponseEntity.ok(Map.of(
                    "message", "posts_fetched",
                    "data", posts
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", null));
        }
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        try {
            PostDetailResponseDto post = postService.getPostById(postId);
            return ResponseEntity.ok(Map.of(
                    "message", "posts_fetched",
                    "data", List.of(post)
            ));
        } catch (IllegalArgumentException e) {
            if ("post_not_found".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "post_not_found", "data", null));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "invalid_request", "data", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "internal_server_error", "data", null));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto requestDto) {
        try {
            if (requestDto.getTitle() == null || requestDto.getContent() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "invalid_request", "data", null));
            }

            Long userId = 1L; // 🔐 나중에 로그인 유저에서 가져오기 (지금은 하드코딩)
            PostCreateResponseDto response = postService.createPost(requestDto, userId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "post_created", "data", response));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("unauthorized")) {
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