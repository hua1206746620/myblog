package com.myblog.myblog.controller;

import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.entity.User;
import com.myblog.myblog.service.BlogService;
import com.myblog.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar; //"/images/游客头像.jpg"

    @GetMapping("/comments/{blogId}")  //展示留言
    public String comments(@PathVariable Long blogId, Model model){
        // 获取父评论
        //model.addAttribute("comments", commentService.getFatherComments(blogId));
        // 获取所有评论
        //model.addAttribute("comments", commentService.getAllComments(blogId));

        model.addAttribute("comments", commentService.getHandleComments(blogId));
        // 详情博客内容: sql语句示意 多表的查询，根据 blogtags中间表 和 user表获得
        // blog.user_id = user.id
        // and blog.blogId = blogTags.blogId
        // and  tag.id = blogTags.tagId
        // and  b.id = #{id}
        model.addAttribute("blog", blogService.getDetailedBlog(blogId));



        return "blog :: commentList";
    }

    @PostMapping("/comments")   //提交留言
    public String post(Comment comment, HttpSession session, @RequestParam("parentComment.id") Long parentCommentId){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getDetailedBlog(blogId));  //绑定博客与评论
        comment.setBlogId(blogId);

        System.out.println(parentCommentId);
        comment.setParentCommentId(parentCommentId);// 设置父ID

        User user = (User) session.getAttribute("user");
        if (user != null){//用户为管理员

            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            comment.setAvatar(avatar);
        }

        //System.out.println(comment);
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }



}
