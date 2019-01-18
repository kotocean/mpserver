package com.example.mpserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhanghd16 on 2018/5/21.
 */
@Controller
public class HomeController {
    @RequestMapping("/index")
    public String home(Model model){
        return "index";
    }
}
