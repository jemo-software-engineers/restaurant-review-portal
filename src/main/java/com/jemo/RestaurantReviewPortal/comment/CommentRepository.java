package com.jemo.RestaurantReviewPortal.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByReviewIdAndStatus(Long reviewId, CommentStatus status);

    Comment findByReviewIdAndId(Long reviewId, Long commentId);

    List<Comment> findAllByStatus(CommentStatus commentStatus);
}
