package com.myblog.myblog.service.impl;

import com.myblog.myblog.dao.BlogDao;
import com.myblog.myblog.entity.Blog;
import com.myblog.myblog.entity.BlogAndTag;
import com.myblog.myblog.entity.Tag;
import com.myblog.myblog.exception.NotFoundException;
import com.myblog.myblog.service.BlogService;
import com.myblog.myblog.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogDao blogDao;

    @Override //后端展示单条
    public Blog getBlog(Long id) {
        return blogDao.getBlog(id);
    }

    @Override //后端展示所有
    public List<Blog> getAllBlog() { // 通过typeId进行连表查询
        return blogDao.getAllBlog();
    }

    @Override //后端条件查询展示所有
    public List<Blog> searchAllBlog(Blog blog) {
        return blogDao.searchAllBlog(blog);
    }

    @Override    //后端新增博客
    public int saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0); //设置浏览次数
        //保存博客
        blogDao.saveBlog(blog);
        //保存博客后才能获取自增的id
        Long id = blog.getId();
        //将标签的数据存到t_blogs_tag表中 tags更新
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;
        for (Tag tag : tags) {
            //新增时无法获取自增的id,在mybatis里修改
            blogAndTag = new BlogAndTag(tag.getId(), id);
            blogDao.saveBlogAndTag(blogAndTag);
        }
        return 1;
    }

    @Override   //后端编辑博客
    public int updateBlog(Blog blog) {
        // 更新时间
        blog.setUpdateTime(new Date());
        // 将标签的数据存到t_blogs_tag表中
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;
        for (Tag tag : tags) {
            blogAndTag = new BlogAndTag(tag.getId(), blog.getId());
            blogDao.saveBlogAndTag(blogAndTag);
        }
        return blogDao.updateBlog(blog);
    }

    @Override //后端根据标题、分类、推荐搜索博客
    public int deleteBlog(Long id) {
        return blogDao.deleteBlog(id);
    }

    // 下面是前台展示

    @Transactional
    @Override //前端展示
    public Blog getDetailedBlog(Long id) {
        Blog blog = blogDao.getDetailedBlog(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        String content = blog.getContent();
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));  //将Markdown格式转换成html
        // 浏览次数
        blogDao.updateView(id);
        return blog;
    }

    @Override
    public List<Blog> getByTypeId(Long typeId) {
        return blogDao.getByTypeId(typeId);
    }

    @Override
    public List<Blog> getByTagId(Long tagId) {
        return blogDao.getByTagId(tagId);
    }

    @Override //主页博客展示
    public List<Blog> getIndexBlog() {
        return blogDao.getIndexBlog();
    }

    @Override
    public List<Blog> getAllRecommendBlog() {
        return blogDao.getAllRecommendBlog();
    }

    @Override //全局搜索博客
    public List<Blog> getSearchBlog(String query) {
        return blogDao.getSearchBlog(query);
    }

    @Override //归档博客
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findGroupYear();
        Set<String> set = new HashSet<>(years);  //set去掉重复的年份
        //年份降序排列
        Map<String, List<Blog>> map = new TreeMap<>((o1, o2) -> Integer.parseInt(o2)-Integer.parseInt(o1));
        for (String year : set) {
            map.put(year, blogDao.findByYear(year));
        }

        return map;
    }

    @Override
    public int countBlog() {
        return blogDao.getAllBlog().size();
    }



}
