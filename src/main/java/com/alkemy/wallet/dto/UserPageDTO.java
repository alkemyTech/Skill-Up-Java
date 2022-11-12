package com.alkemy.wallet.dto;

import jdk.jfr.Name;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;
@Data
public class UserPageDTO {
    String nextPage;
    String previusPage;
    int totalPages;
    List<UserDTO> userDTOList;
}
