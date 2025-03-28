package daisy.community_be.service;

import daisy.community_be.domain.Post;
import daisy.community_be.domain.User;
import daisy.community_be.dto.request.PostListResponseDto;
import daisy.community_be.dto.response.PostDetailResponseDto;
import daisy.community_be.repository.PostRepository;
import daisy.community_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import daisy.community_be.dto.request.PostCreateRequestDto;
import daisy.community_be.dto.response.PostCreateResponseDto;
import java.time.LocalDateTime;
import daisy.community_be.dto.request.PostUpdateRequestDto;
import daisy.community_be.dto.response.PostUpdateResponseDto;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostListResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> new PostListResponseDto(
                        post.getId(),
                        post.getTitle(),
                        new PostListResponseDto.AuthorDto(
                                post.getUser().getNickname(),
                                post.getUser().getProfileImageUrl()
                        ),
                        post.getLikeCount(),
                        post.getCommentCount(),
                        post.getViewCount(),
                        post.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public PostDetailResponseDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));

        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                new PostDetailResponseDto.AuthorDto(
                        post.getUser().getNickname(),
                        post.getUser().getProfileImageUrl()
                ),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getViewCount(),
                post.getCreatedAt()
        );
    }

    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("unauthorized"));

        Post post = new Post();
        post.setUser(user);
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setImageUrl(requestDto.getPostImage());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setViewCount(0);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return new PostCreateResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getCreatedAt()
        );
    }

    public PostUpdateResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("forbidden");
        }

        if (requestDto.getTitle() != null) post.setTitle(requestDto.getTitle());
        if (requestDto.getContent() != null) post.setContent(requestDto.getContent());
        if (requestDto.getPostImage() != null) post.setImageUrl(requestDto.getPostImage());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);

        return new PostUpdateResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getProfileImageUrl(),
                post.getUpdatedAt()
        );
    }

    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("forbidden");
        }

        postRepository.delete(post);
    }
}