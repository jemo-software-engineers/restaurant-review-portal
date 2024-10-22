package com.jemo.RestaurantReviewPortal.comment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponse {
    private Long commentId;
    private String commentText;
    private Long reviewId;
    private Long userId;
    private CommentStatus status;
}
