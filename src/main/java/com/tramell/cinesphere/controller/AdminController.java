package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.service.AdminService;
import com.tramell.cinesphere.util.ApiResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tramell/cinesphere/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        return ApiResponseUtil.success(adminService.getDashboardStats(), "Dashboard stats retrieved");
    }

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
}
