package com.myblog.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutMeController {
    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
