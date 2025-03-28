package daisy.community_be.service;

import daisy.community_be.domain.Comment;
import daisy.community_be.domain.Post;
import daisy.community_be.domain.User;
import daisy.community_be.dto.request.CommentCreateRequestDto;
import daisy.community_be.dto.response.CommentCreateResponseDto;
import daisy.community_be.dto.response.CommentListResponseDto;
import daisy.community_be.repository.CommentRepository;
import daisy.community_be.repository.PostRepository;
import daisy.community_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentCreateResponseDto createComment(Long postId, CommentCreateRequestDto requestDto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("unauthorized"));

        Comment comment = new Comment();
        comment.setContent(requestDto.getContent());
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        return new CommentCreateResponseDto(
                saved.getId(),
                saved.getContent(),
                new CommentCreateResponseDto.AuthorDto(
                        user.getNickname(),
                        user.getProfileImageUrl()
                ),
                saved.getCreatedAt()
        );
    }

    public List<CommentListResponseDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));

        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        return comments.stream()
                .map(comment -> new CommentListResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        new CommentListResponseDto.AuthorDto(
                                comment.getUser().getNickname(),
                                comment.getUser().getProfileImageUrl()
                        ),
                        comment.getCreatedAt()
                )).collect(Collectors.toList());
    }
}