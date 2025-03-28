package daisy.community_be.service;

import daisy.community_be.domain.Post;
import daisy.community_be.domain.PostLike;
import daisy.community_be.domain.User;
import daisy.community_be.dto.response.PostLikeResponseDto;
import daisy.community_be.repository.PostLikeRepository;
import daisy.community_be.repository.PostRepository;
import daisy.community_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    public PostLikeResponseDto likePost(Long postId, Long userId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("post_not_found");
        }

        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new IllegalArgumentException("already_liked");
        }

        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        PostLike like = new PostLike();
        like.setPost(post);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now());

        PostLike saved = postLikeRepository.save(like);

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        return new PostLikeResponseDto(saved.getId(), postId, saved.getCreatedAt());
    }

    public void unlikePost(Long postId, Long userId) {
        // 게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));

        // 좋아요 여부 확인
        PostLike like = postLikeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));

        // 삭제
        postLikeRepository.delete(like);

        // 게시글의 좋아요 수 감소
        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);
    }
}