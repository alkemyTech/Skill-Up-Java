package com.alkemy.wallet.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPaginatedDto {

    private List<UserDto> userList;
    private String nextUrl;
    private String previousUrl;

}
