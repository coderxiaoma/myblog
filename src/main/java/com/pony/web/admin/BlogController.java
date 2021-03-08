package com.pony.web.admin;

import com.pony.domain.Blog;
import com.pony.domain.User;
import com.pony.service.BlogService;
import com.pony.service.TagService;
import com.pony.service.TypeService;
import com.pony.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author pony
 * @date 2020/9/10
 */
@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    BlogService service;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;

    //获取所有博客
    @RequestMapping("/blogs")
    public String blogs(@PageableDefault(size = 2, sort = "updateTime", direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", service.listBlog(pageable, blog));
        return "admin/blogs";
    }

    //搜索博客
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 10, sort = "updateTime", direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", service.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    //新增博客
    @GetMapping("blogs/add")
    public String add(Model model,HttpSession session) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        session.setAttribute("edit", "add");
        return "admin/blogs-input";
    }

    //编辑博客
    @GetMapping("blogs/edit/{id}")
    public String edit(@PathVariable long id, Model model,HttpSession session) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        Blog blog = service.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        session.setAttribute("edit","modify");
        return "admin/blogs-input";
    }

    //提交
    @PostMapping("/blogs/post")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session, Model model) {

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog blog1;
        if (session.getAttribute("edit")=="add") {  //添加
            blog.setStartTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            blog1 = service.saveBlog(blog);
        } else {  //修改
            blog.setUpdateTime(new Date());
            blog1 =service.saveBlog(blog);
        }
        if(blog1==null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/blogs";
    }


    @GetMapping("/blogs/delete/{id}")
    public String delete(@PathVariable long id,RedirectAttributes attributes){
        service.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }
}
