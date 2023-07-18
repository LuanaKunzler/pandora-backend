package com.pandora.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.PermitAll;

@Controller
@RequestMapping("/api/public")
@PermitAll
@CrossOrigin
public class PublicApiController {
}
