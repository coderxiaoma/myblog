package com.pony.web.admin;

import com.pony.NotFoundException;
import com.pony.domain.Tag;
import com.pony.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author pony
 * @date 2020/9/11
 */
@Controller
@RequestMapping("/admin/tags")
public class TagController {

    @Autowired
    private TagService service;

    //获得所有标签
    @GetMapping("")
    public String tags(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                               Pageable pageable, Model model) {
        model.addAttribute("page", service.listTag(pageable));
        return "admin/tags";
    }

    //新增标签,新建一个tag对象并写入到model中去
    @GetMapping("/add")
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("tag", new Tag());
        model.addAttribute("edit", "add");
        return "admin/tag-input";
    }

    //编辑标签
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model) {
        Tag tag = service.getTag(id);
        if (tag != null) {
            model.addAttribute("tag", tag);
            model.addAttribute("edit", "modify");
        }
        return "admin/tag-input";
    }

    //提交
    @PostMapping("/add")
    public String post(@Valid Tag tag, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        Tag t = (Tag) model.getAttribute("tag");

        //判断是否已经存在
        Tag tag1 = service.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "该标签已经存在");
        }
        if (result.hasErrors()) {
            return "admin/tag-input";
        }


        //根据Model中的edit属性判断是新增还是修改
        String edit = (String) model.getAttribute("edit");
        Tag tag2;
        if (edit == "modify") {  //修改
            tag2 = service.updateTag(t.getId(),tag);
        } else {                 //新增
            tag2 = service.saveTag(tag);
        }
        if (tag2 == null)
            redirectAttributes.addFlashAttribute("message", "操作失败");
        else
            redirectAttributes.addFlashAttribute("message", "操作成功");
        return "redirect:/admin/tags";
    }


    //删除
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id,RedirectAttributes redirectAttributes){
        service.deleteTag(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    };
}
