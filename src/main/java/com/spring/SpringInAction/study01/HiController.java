package com.spring.SpringInAction.study01;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 세션에 order 라는 이름의 변수를 추가할 예정
// 이 어노테이션이 붙은 컨트롤러에서 Model 에 @SessionAttributes("변수명") 에 있는 변수명과 같은 이름으로
// Model 에 값을 넣는다면(ex. @SessionAttributes("변수명1"), model.addAttribute("변수명1", 값))
// 세션에 해당 변수명으로 값이 저장 됨.
// 여러화면에 걸쳐서 값을 입력받아야 완성되는 데이터의 경우에 사용하면 좋다고 한다. (ex. 자소서 입력할때 인적사항 -> 자기소개 -> 최종제출 할때 그느낌쿠)
// 추가적으로 이 어노테이션을 사용하고 모델이 값을 넣었으면, 다른 컨트롤러에서 이 어노테이션을 사용하여 모델에 넣은 값의 변수명과 같은 변수명을 사용하면
// 모델엔 앞서 이 어노테이션을 사용하여 넣었던 값이 담겨있다.
// 세션에 값이 담기고, 이걸 통해서 담긴 세션 값들은 모델에도 들어가서 모델 전역변수가 되어 값을 어디서든 사용할 수 있다고 생각하면 편할 듯.

@SessionAttributes("sessionData")
@Controller
public class HiController {

    @GetMapping("/session/{data}")
    public String setSessionAttribute(@PathVariable("data") String data, Model model) {
        // @SessionAttributes 에 선언한 변수와 같은 이름으로 모델이 값 넣기
        model.addAttribute("sessionData", data);
        return "/hi";
    }
    @GetMapping("/hi/session/data")
    public String getSessionAttributes(HttpServletRequest httpServletRequest) {
        // @SessionAttributes 에 선언한 변수 이름으로 자동으로 모델에 값 들어가있음
        return "/hi_data";
    }

}
