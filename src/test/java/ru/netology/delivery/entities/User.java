package ru.netology.delivery.entities;

import lombok.Value;

public class User {
    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
