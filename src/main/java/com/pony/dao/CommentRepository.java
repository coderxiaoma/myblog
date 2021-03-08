package com.pony.dao;

import com.pony.domain.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pony
 * @date 2020/9/17
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    //根据博客id查询评论列表
     List<Comment> findCommentsByBlogIdAndParentCommentIsNull(long blogId, Sort sort);

}
