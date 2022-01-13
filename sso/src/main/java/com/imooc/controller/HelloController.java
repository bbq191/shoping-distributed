package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/** @author afu */
@Controller
public class HelloController {
  @GetMapping("/hello")
  @ResponseBody
  public Object hello() {
    return "Hello world";
  }
}
