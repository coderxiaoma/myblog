package com.pony.web.admin;

import com.pony.domain.Type;
import com.pony.service.TypeService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author pony
 * @date 2020/9/10
 * 分类 控制层
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService service;

    //显示分类
    @GetMapping("/types")
    public String list(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                               Pageable pageable, Model model) {
        model.addAttribute("page", service.listType(pageable));
        return "admin/types";
    }

    //新增分类提交
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes redirectAttributes) {

        //重复验证
        Type type1 = service.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","分类已经存在");
        }

        //非空验证和重复验证，完成，需要引入JSR-380规范和相应的hibernate实现
        if(result.hasErrors()){
            return "admin/type-input";
        }

        Type t = service.saveType(type);
        if (t == null) {
            redirectAttributes.addFlashAttribute("message", "添加失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "添加成功");
        }
        return "redirect:/admin/types";
    }

    //更新分类提交
    @PostMapping("/types/{id}")
    public String modifyType(@Valid Type type,BindingResult result,@PathVariable long id,RedirectAttributes redirectAttributes){
        Type type1 = service.getTypeByName(type.getName());
        if(type1!=null){
            result.rejectValue("name","nameError","分类已经存在");
        }
        if(result.hasErrors()){
            return "admin/type-input";
        }
        Type t = service.updateType(id, type);
        if (t == null) {
            redirectAttributes.addFlashAttribute("message", "更新失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }

    //新增分类界面
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",service.getType(id));
        return "admin/type-input";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable  long id,RedirectAttributes redirectAttributes){
        service.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}
