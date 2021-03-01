package com.rosti.messenger.controllers;

import com.rosti.messenger.model.Messages;
import com.rosti.messenger.model.User;
import com.rosti.messenger.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Value(("${upload.path}"))
    private String uploadPath;
  @Autowired
    MessageRepo messageRepo;
    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = " ") String filter, Model model){

        Iterable<Messages> messages =null;

        if (filter.isEmpty()&&!filter.isEmpty()){
            messages = messageRepo.findByTag(filter);
        }else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }
    @PostMapping("/main")
    public String  saveMessage(@AuthenticationPrincipal User user,
                                @RequestParam String text,
                               @RequestParam String tag,
                               @RequestParam("file") MultipartFile file,
                               Map<String, Object> model) throws IOException {
        Messages message = new Messages(text,tag,user);
        if (file!=null && !file.getOriginalFilename().isEmpty()){
            File uploadDire = new File(uploadPath);
            if (!uploadDire.exists()){
                uploadDire.mkdir();
            }
            String uidFile = UUID.randomUUID().toString();
            String resultFileName = uidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);

        }
        messageRepo.save(message);
        Iterable<Messages> messages = messageRepo.findAll();
        model.put("messages", messages);
        return "main";
    }
}

