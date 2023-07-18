package com.pandora.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.security.PermitAll;

@Controller
@RequestMapping("/swagger")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class IndexController {

    @GetMapping
    public RedirectView redirectToSwaggerUi() {
        return new RedirectView("/swagger-ui.html");
    }
}
