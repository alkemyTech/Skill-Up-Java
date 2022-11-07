package com.alkemy.wallet.model.mapper.response;

import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserListResponseDto {

    private List<UserResponseDto> users;
}
