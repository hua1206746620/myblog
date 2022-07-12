package com.myblog.myblog.service;

import com.myblog.myblog.entity.Comment;

import java.util.List;

public interface CommentService {
    // 得到父评论
    List<Comment> getFatherComments(Long blogId);

    // 得到子评论
    List<Comment> getSonComments(Long blogId, Long parentId);

    int saveComment(Comment comment);

    List<Comment> getAllComments(Long blogId);

    List<Comment> getHandleComments(Long blogId);
}
