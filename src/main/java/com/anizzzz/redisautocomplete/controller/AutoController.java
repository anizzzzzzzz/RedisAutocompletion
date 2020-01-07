package com.anizzzz.redisautocomplete.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutoController {
    @GetMapping("/")
    public String index(){return "index";}
}
