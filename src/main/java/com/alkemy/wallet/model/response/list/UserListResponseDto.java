package com.alkemy.wallet.model.response.list;

import com.alkemy.wallet.model.response.UserResponseDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class UserListResponseDto {

    private List<UserResponseDto> users;
}
