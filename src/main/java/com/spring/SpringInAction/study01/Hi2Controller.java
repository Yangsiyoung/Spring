package com.spring.SpringInAction.study01;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

//@SessionAttributes("sessionData")
@Controller
public class Hi2Controller {
    @GetMapping("/hi2")
    public String getSessionData(@ModelAttribute(name = "ingredient") String ingredient) {
        // @ModelAttribute 덕분에 직접적으로 model.addAttribute 하지 않아도 됨
        return "/hi2_data";
    }
}
