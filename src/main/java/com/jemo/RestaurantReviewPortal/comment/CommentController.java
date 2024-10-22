package com.jemo.RestaurantReviewPortal.comment;

import com.jemo.RestaurantReviewPortal.user.User;
import com.jemo.RestaurantReviewPortal.user.UserRole;
import com.jemo.RestaurantReviewPortal.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    // Get all comments for a review
    @GetMapping("/api/reviews/{reviewId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForReviews(@PathVariable Long reviewId) {
        List<Comment> comments = commentService.findAllByReviewId(reviewId);

        return getCommentListResponseEntity(comments);
    }


    // Create new comment for a review
    @PostMapping("/api/reviews/{reviewId}/comments")
    public ResponseEntity<String> createComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId, @Valid @RequestBody CommentRequest commentRequest) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        Comment comment = commentService.createCommentForReview(reviewId, commentRequest, authenticatedUser);
        if(comment != null) {
            return new ResponseEntity<>("Comment Created Successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Comment Creation Failed", HttpStatus.BAD_REQUEST);
    }

    // Update a comment
    @PutMapping("/api/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<String> updateComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId, @PathVariable Long commentId, @Valid @RequestBody CommentRequest commentRequest) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        Comment commentToUpdate = commentService.findByReviewIdAndId(reviewId, commentId);
        if(commentToUpdate != null) {
            if(commentToUpdate.getUser().equals(authenticatedUser) || authenticatedUser.getRole().equals(UserRole.ADMIN)) {
                Boolean updated = commentService.updateComment(commentToUpdate, authenticatedUser, commentRequest);
                if(updated) {
                    return new ResponseEntity<>("Comment Updated Successfully", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Comment Updated Failed", HttpStatus.BAD_REQUEST);
    }

    // Delete comment
    @DeleteMapping("/api/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId, @PathVariable Long commentId) {
        Comment commentToDelete = commentService.findByReviewIdAndId(reviewId, commentId);
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        if(commentToDelete == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if(commentToDelete.getUser().getUsername().equals(authenticatedUser.getUsername()) || authenticatedUser.getRole().equals(UserRole.ADMIN)) {
                Boolean deleted = commentService.deleteById(commentId);
                if(deleted) {
                    return new ResponseEntity<>("Comment Deleted Successfully", HttpStatus.OK);
                }
                return new ResponseEntity<>("Comment Deletion Failed", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // Get a list of all pending reviews due for approval
    @GetMapping("/admin/api/comments")
    public ResponseEntity<List<CommentResponse>> getAllPendingComments() {
        List<Comment> comments = commentService.findAllByPendingStatus();
        return getCommentListResponseEntity(comments);
    }


    // admin approve comment
    @PutMapping("/admin/api/comments/{commentId}/approve")
    public ResponseEntity<String> approveComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long commentId) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        if(authenticatedUser.getRole().equals(UserRole.ADMIN)) {
            Boolean approved = commentService.approveComment(commentId);
            if(approved) {
                return new ResponseEntity<>("Comment Approved Successfully", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Review Approval Failed", HttpStatus.BAD_REQUEST);
    }



    // admin reject comment
    @PutMapping("/admin/api/comments/{commentId}/reject")
    public ResponseEntity<String> rejectComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long commentId) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        if(authenticatedUser.getRole().equals(UserRole.ADMIN)) {
            Boolean rejected = commentService.rejectComment(commentId);
            if(rejected) {
                return new ResponseEntity<>("Comment Rejected Successfully", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Review Rejection Failed", HttpStatus.BAD_REQUEST);
    }




    private ResponseEntity<List<CommentResponse>> getCommentListResponseEntity(List<Comment> comments) {
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    CommentResponse commentResponse = new CommentResponse();
                    commentResponse.setCommentId(comment.getId());
                    commentResponse.setCommentText(comment.getCommentText());
                    commentResponse.setReviewId(comment.getReview().getId());
                    commentResponse.setUserId(comment.getUser().getId());
                    commentResponse.setStatus(comment.getStatus());
                    return commentResponse;
                })
                .toList();
        return new ResponseEntity<>(commentResponses, HttpStatus.OK);
    }

}
