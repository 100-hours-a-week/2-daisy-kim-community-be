package daisy.community_be.service;

import daisy.community_be.domain.Post;
import daisy.community_be.dto.request.PostListResponseDto;
import daisy.community_be.dto.response.PostDetailResponseDto;
import daisy.community_be.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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
}