package com.myblog.myblog;

import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyBlogApplicationTests {

    @Autowired
    CommentService commentService;

    @Test
    void contextLoads() {
        List<Comment> fatherComments = commentService.getFatherComments(11L);
        System.out.println("所有父评论：" + fatherComments);
        List<Comment> sonComments = commentService.getSonComments(11L, 11L);
        System.out.println("id11评论的所有子评论：" + sonComments);
        System.out.println();

        List<Comment> handleComments = commentService.getHandleComments(11L);

        for (Comment handleComment : handleComments) {
            System.out.println();
            //System.out.println(handleComment.getId() + "的子评论:" + handleComment.getReplyComments());
            for (Comment replyComment : handleComment.getReplyComments()) {
                System.out.println(handleComment.getId() + "的子评论:" +replyComment);
                System.out.println(replyComment.getParentNickname());
            }
        }
    }

}
