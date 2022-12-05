package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exception.ResourceFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface ICustomUserDetailsService {
    List<ResponseUserDto> findAll();

    Page<ResponseUserDto> findAllPageable(Pageable pageable);

    Boolean existsById(Long id);

    ResponseUserDto findByEmail(String email);

    ResponseUserDto save(@Valid ResponseUserDto responseUserDto) throws ResourceFoundException;

    ResponseUserDto update(ResponseUserDto responseUserDto);
}
