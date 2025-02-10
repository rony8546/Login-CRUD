package com.ronaldo.crudlogin.dto;

import lombok.Data;

@Data
public class ProductResponse {
    private String name;
    private String descripcion;
    private float price;
    private int quantity;
    private String status;
    private String message;
}
