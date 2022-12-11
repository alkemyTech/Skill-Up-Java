package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

public interface ICustomUserDetailsService {

    List<ResponseUserDto> findAll();

    Boolean existsById(Long id);

    ResponseUserDto saveAdmin(@Valid RequestUserDto requestUserDto) throws ResourceFoundException;

    ResponseUserDto update(RequestUserDto requestUserDto) throws ResourceNotFoundException;

    ResponseUserDto findByEmail(String email);

    ResponseUserDto save(RequestUserDto requestUserDto);

    @Transactional
    Page<ResponseUserDto> findAllUsersPageable(int page) throws Exception;

    ResponseUserDto getUserAuthenticated();

    ResponseUserDto getUserLoggedById(Long id);

}
