package com.pony.web;

import com.pony.NotFoundException;
import com.pony.domain.Blog;
import com.pony.service.BlogService;
import com.pony.service.TagService;
import com.pony.service.TypeService;
import com.pony.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author pony
 * @date 2020/9/8
 */
@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 10, sort = "updateTime", direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
        model.addAttribute("blogs",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }


    @GetMapping("/blog/{id}")
    public String blog(@PathVariable long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }


    @PostMapping("/search")
    public String search(@PageableDefault(size = 10, sort = "updateTime", direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("blogs", blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }


    @GetMapping("/footer/latestBlogs")
    public String latestBlogs(Model model){
        model.addAttribute("latestBlogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: latestBlogList";
    }
}
