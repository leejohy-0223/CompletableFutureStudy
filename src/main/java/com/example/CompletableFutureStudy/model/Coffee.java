package com.example.CompletableFutureStudy.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coffee {
    private String name;
    private int price;
}
