package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.request.UserUpdateRequest;
import com.tramell.cinesphere.dto.response.UserResponse;

import java.util.List;
import com.tramell.cinesphere.enums.Role;

public interface UserService {
    UserResponse updateProfile(Long userId, UserUpdateRequest request);
    List<UserResponse> getAllUsers();
    UserResponse updateUserRole(Long userId, Role role);
    void deleteUser(Long userId);
}
