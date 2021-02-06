package com.ujuji.navigation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job")
public class JobController {

    @GetMapping("/alive")
    public String alive() {
        return "I am alive";
    }
}
