package com.pony.service;

import com.pony.domain.Comment;

import java.util.List;

/**
 * @author pony
 * @date 2020/9/17
 */
public interface CommentService {
    List<Comment> listCommentByBlogId(long blogId);
    Comment saveComment(Comment comment);
}
