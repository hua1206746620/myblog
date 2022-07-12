package com.myblog.myblog.service.impl;

import com.myblog.myblog.dao.BlogDao;
import com.myblog.myblog.dao.CommentDao;
import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private BlogDao blogDao;

    @Override // 所有父评论
    public List<Comment> getFatherComments(Long blogId) {
        //没有父节点的默认为-1
        List<Comment> comments = commentDao.findByBlogIdAndParentCommentNull(blogId, Long.parseLong("-1"));
        return comments;
    }

    @Override
    public int saveComment(Comment comment) {
        //获得父id
        Long parentCommentId = comment.getParentComment().getId();
        //没有父级评论默认是-1
        if (parentCommentId != -1) {
            //拿到父级评论 并且 set
            Comment fatherComment = commentDao.findByParentCommentId(comment.getParentCommentId());
            comment.setParentComment(fatherComment);
            if (fatherComment == null) comment.setParentNickname("");
            else comment.setParentNickname(fatherComment.getNickname());
        } else {
            //没有父级评论
            comment.setParentCommentId((long) -1);
            comment.setParentComment(null);
        }
        // 存入自己的子评论
        comment.setReplyComments(getSonComments(comment.getBlogId(),comment.getId()));

        comment.setCreateTime(new Date());
        return commentDao.saveComment(comment);
    }

    //得到某个 parentId 父评论的所有子评论
    @Override
    public List<Comment> getSonComments(Long blogId, Long parentId) {
        List<Comment> sonComments = commentDao.findByBlogIdAndParentCommentNull(blogId, parentId);
        if (sonComments == null){
            return new ArrayList<Comment>();
        }
        return sonComments;
    }

    //得到所有评论
    @Override
    public List<Comment> getAllComments(Long blogId) {
        return commentDao.findAllCommentsByBlogId(blogId);
    }

    // 由父评论组成的List集合，里面的每一个父评论 的 子评论List只有一层
    @Override
    public List<Comment> getHandleComments(Long blogId) {
        // 得到所有的父评论
        List<Comment> fatherComments = getFatherComments(blogId);
        // 这里处理完的 fatherComments 中 每一个 父comment 的 replyComments 都是只有一层的，没有出现嵌套
        return  eachComment(fatherComments);
    }

    //循环每个父评论节点 处理完子节点后 再返回
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        // 合并评论的各层子评论 只保留一层
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> sonComments = getSonComments(comment.getBlogId(),comment.getId());

            comment.setReplyComments(sonComments);
            for (Comment sonComment : sonComments) {
                sonComment.setParentNickname(comment.getNickname());
                // 循环迭代，找出子代，存放tempReply中
                recursively(sonComment);
            }
            comment.setReplyComments(tempReplies);
            tempReplies = new ArrayList<>();
        }
    }

    private List<Comment> tempReplies = new ArrayList<>();
    //下面这段代表就是把可能存在嵌套的子评论 转换为一层的子评论 放入tempReplies
    // 这里没用到 getReplyComments 而是直接操作的数据库会影响性能，需要改进
    private void recursively(Comment comment){
        tempReplies.add(comment); // 顶节点添加到临时存放集合
        if (getSonComments(comment.getBlogId(),comment.getId()).size()>0){
            //List<Comment> replies = comment.getReplyComments();
            List<Comment> replies = getSonComments(comment.getBlogId(),comment.getId());
            for (Comment reply : replies) {
                //
                reply.setParentNickname(comment.getNickname());
                tempReplies.add(reply);
                if (getSonComments(reply.getBlogId(),reply.getId()).size()>0){
                    recursively(reply);
                }
            }
        }
    }


}
