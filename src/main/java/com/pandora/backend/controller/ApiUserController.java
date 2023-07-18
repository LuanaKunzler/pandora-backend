package com.pandora.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@CrossOrigin
public class ApiUserController {
}
