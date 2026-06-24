package com.codewithmosh.store.controller;

import com.codewithmosh.store.entities.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Getter
public class MessageController {

    @RequestMapping("/hello")
    public Message hello() {
        return new Message("Hello Mohammad Hani");
    }
}
