package com.spring.SpringInAction.tacos.web.order;

import com.spring.SpringInAction.tacos.domain.ingredient.Ingredient;
import com.spring.SpringInAction.tacos.domain.ingredient.IngredientRepository;
import com.spring.SpringInAction.tacos.domain.order.Order;
import com.spring.SpringInAction.tacos.domain.taco.Taco;
import com.spring.SpringInAction.tacos.domain.taco.TacoRepository;
import com.spring.SpringInAction.tacos.domain.user.TacoUser;
import com.spring.SpringInAction.tacos.domain.user.TacoUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/design")
// 세션에 order 라는 이름의 변수를 추가할 예정
// 이 어노테이션이 붙은 컨트롤러에서 Model 에 @SessionAttribute("변수명") 에 있는 변수명과 같은 이름으로
// Model 에 값을 넣는다면(ex. @SessionAttribute("변수명1"), model.addAttribute("변수명1", 값))
// 세션에 해당 변수명으로 값이 저장 됨.
// 여러화면에 걸쳐서 값을 입력받아야 완성되는 데이터의 경우에 사용하면 좋다고 한다. (ex. 자소서 입력할때 인적사항 -> 자기소개 -> 최종제출 할때 그느낌쿠)
// 추가적으로 이 어노테이션을 사용하고 모델이 값을 넣었으면, 다른 컨트롤러에서 이 어노테이션을 사용하여 모델에 넣은 값의 변수명과 같은 변수명을 사용하면
// 모델엔 앞서 이 어노테이션을 사용하여 넣었던 값이 담겨있다.
// 세션에 값이 담기고, 이걸 통해서 담긴 세션 값들은 모델에도 들어가서 모델 전역변수가 되어 값을 어디서든 사용할 수 있다고 생각하면 편할 듯.
@SessionAttributes("order")
@RequiredArgsConstructor
@Controller
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepo;
    private final TacoUserRepository tacoUserRepository;

    // 세션에 order 라는 이름으로 order 객체 초기화
    @ModelAttribute("order")
    public Order initOrderSessionValue() {
        Order order = new Order();
        order.setDeliveryName("initOrder");
        return order;
    }

    // 세션에 taco 라는 이름으로 taco 객체 초기화
    @ModelAttribute("taco")
    public Taco initTacoSessionValue() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model, Principal principal) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredients::add);

        Ingredient.Type[] types = Ingredient.Type.values();
        for(Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    getIngredientsByType(ingredients, type));
        }

        TacoUser tacoUser = tacoUserRepository.findByUsername(principal.getName()).get();
        model.addAttribute("user", tacoUser);
        model.addAttribute("taco", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Validated Taco design, Errors errors, @ModelAttribute(value = "order") Order order) {
        if(errors.hasErrors()) {
            return "design";
        }
        order.addTaco(design);
        tacoRepo.save(design);
        return "redirect:/orders/current";
    }

    private List<Ingredient> getIngredientsByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients.stream().filter((ingredient -> ingredient.getType() == type)).collect(Collectors.toList());
    }
}
