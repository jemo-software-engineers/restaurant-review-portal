package com.jemo.RestaurantReviewPortal.comment;

import com.jemo.RestaurantReviewPortal.review.Review;
import com.jemo.RestaurantReviewPortal.review.ReviewRepository;
import com.jemo.RestaurantReviewPortal.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    public List<Comment> findAllByReviewId(Long reviewId) {
        return commentRepository.findAllByReviewIdAndStatus(reviewId, CommentStatus.valueOf("APPROVED"));
    }

    public Comment createCommentForReview(Long reviewId, CommentRequest commentRequest, User authenticatedUser) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            return null;
        }
        Comment comment = Comment.builder()
                .review(review)
                .commentText(commentRequest.commentText())
                .user(authenticatedUser)
                .status(CommentStatus.PENDING)
                .build();
        Comment newComment = commentRepository.save(comment);
        if (newComment != null) {
            return newComment;
        }
        return null;
    }

    public Comment findByReviewIdAndId(Long reviewId, Long commentId) {
        return commentRepository.findByReviewIdAndId(reviewId, commentId);
    }

    public Boolean deleteById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            commentRepository.delete(comment);
            return true;
        }
        return false;
    }


    public Boolean updateComment(Comment commentToUpdate, User authenticatedUser, @Valid CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setReview(commentToUpdate.getReview());
        comment.setUser(authenticatedUser);
        comment.setId(commentToUpdate.getId());
        comment.setStatus(CommentStatus.PENDING);
        comment.setCommentText(commentRequest.commentText() != null ? commentRequest.commentText() : commentToUpdate.getCommentText());
        commentRepository.save(comment);
        return true;
    }

    public Boolean rejectComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setStatus(CommentStatus.REJECTED);
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

    public Boolean approveComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setStatus(CommentStatus.APPROVED);
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

    public List<Comment> findAllByPendingStatus() {
        return commentRepository.findAllByStatus(CommentStatus.PENDING);
    }
}
