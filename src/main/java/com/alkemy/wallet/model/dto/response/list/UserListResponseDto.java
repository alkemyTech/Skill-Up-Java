package com.alkemy.wallet.model.dto.response.list;

import com.alkemy.wallet.model.dto.response.UserResponseDto;
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
