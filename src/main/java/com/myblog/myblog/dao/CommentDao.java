package com.myblog.myblog.dao;

import com.myblog.myblog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {

    //根据创建时间倒序来排 根据blogId查询所有的父评论
    List<Comment> findByBlogIdAndParentCommentNull(@Param("blogId") Long blogId, @Param("blogParentId") Long blogParentId);

    //根据 父评论Id 查询 父评论
    Comment findByParentCommentId(@Param("parentCommentId")Long parentcommentid);

    //添加一个评论
    int saveComment(Comment comment);

    //根据 blogId 得到所有评论(父评论、子评论)
    List<Comment> findAllCommentsByBlogId(@Param("blogId") Long blogId);
}
