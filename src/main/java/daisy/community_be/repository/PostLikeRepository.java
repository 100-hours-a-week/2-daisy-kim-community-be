package daisy.community_be.repository;

import daisy.community_be.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);
}