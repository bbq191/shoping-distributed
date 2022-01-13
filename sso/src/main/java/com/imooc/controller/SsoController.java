package com.imooc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** @author afu */
@Controller
public class SsoController {
  @GetMapping("/login")
  public String login(
      String returnUrl, Model model, HttpServletRequest request, HttpServletResponse response) {
    model.addAttribute("returnUrl", returnUrl);
    // 返回 CAS 统一登陆验证
    return "login";
  }
}
