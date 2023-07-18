package com.pandora.backend.controller.adminController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ApiAdminController {
}
