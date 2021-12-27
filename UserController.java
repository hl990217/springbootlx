package cn.ty.controller;

import cn.ty.pojo.User;
import cn.ty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "user/login";
    }

    @RequestMapping("/login")
    public String login(User user, HttpSession session){
        User login = userService.login(user);
        if (login==null){
            return "user/login";
        }else {
            session.setAttribute("user",login);
            return "redirect:/patient/findAll";
        }

    }
}
