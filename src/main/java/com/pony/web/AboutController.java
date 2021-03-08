package com.pony.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author pony
 * @date 2020/9/20
 */
@Controller
public class AboutController {
    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
