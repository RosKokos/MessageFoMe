package com.rosti.messenger.controllers;

import com.rosti.messenger.model.User;
import com.rosti.messenger.model.dto.CaptchaResponseDTO;
import com.rosti.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Autowired
    UserService userService;
    @Value("${recaptcha.secret}")
    private String secret;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @RequestParam("password2") String passwordConfirmation,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model){
        String url = String.format(CAPTCHA_URL,secret, captchaResponse);
        CaptchaResponseDTO response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDTO.class);
        if (!response.isSuccess()){
            model.addAttribute("captchaError", "Fill captcha");
        }
        boolean isEmpty = Strings.isEmpty(passwordConfirmation);
        if (isEmpty){
            model.addAttribute("password2Error", "Password confirmation can not be empty");
        }
        if (user.getPassword() !=null && user.getPassword().equals(passwordConfirmation)){
            model.addAttribute("passwordError", "Passwords are different" );
        }
        if (isEmpty || bindingResult.hasErrors() || !response.isSuccess()){
             Map<String, String> errors = ControllerUtills.getErrors(bindingResult);
             model.mergeAttributes(errors);
             return "registration";
        }
        if (!userService.addUser(user)){
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";

    }
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);
        if (isActivated){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");

        }else {

            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }

}
