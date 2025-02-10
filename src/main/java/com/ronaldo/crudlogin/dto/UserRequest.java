package com.ronaldo.crudlogin.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String role;
    private boolean status;
}
