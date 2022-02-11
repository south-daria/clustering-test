package com.daria.clustering.console.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/clustering/view/v1_0")
@RequiredArgsConstructor
public class ConsoleController {
    @GetMapping(path = "/kmeans")
    public ModelAndView login(Locale locale) {
        return new ModelAndView("kmeans/index.html");
    }

}
