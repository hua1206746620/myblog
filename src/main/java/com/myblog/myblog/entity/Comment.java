package com.myblog.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;
    private String nickname; // 昵称
    private String email;   //邮箱
    private String content; // 评论内容
    private boolean adminComment;  //是否为管理员评论
    private String avatar; // 头像
    private Date createTime; //创建时间



    private Blog blog; // 对应的博客对象
    private Long blogId;


    private Comment parentComment; // 子类对应一个父类评论
    private Long parentCommentId;  //父评论id
    private String parentNickname = "";

    private List<Comment> replyComments = new ArrayList<>(); // 该评论的所有子评论

}