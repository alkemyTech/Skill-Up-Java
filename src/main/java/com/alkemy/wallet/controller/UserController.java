package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.UserModelAssembler;
import com.alkemy.wallet.assembler.model.UserModel;
import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.service.CustomUserDetailsService;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@ApiModel("Controlador de usuario")
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    private final UserModelAssembler userModelAssembler;

    private final PagedResourcesAssembler<ResponseUserDto> pagedResourcesAssembler;


    public UserController(CustomUserDetailsService customUserDetailsService, UserModelAssembler userModelAssembler, PagedResourcesAssembler<ResponseUserDto> pagedResourcesAssembler) {
        this.customUserDetailsService = customUserDetailsService;
        this.userModelAssembler = userModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUserDto> updateUser(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customUserDetailsService.update(requestUserDto));

//        if(!customUserDetailsService.existsById(user.getId())){ //el Id no existe? o no es correcto?
//            System.out.println("Trying to update User without Id");
//            return ResponseEntity.badRequest().body(mapper.getMapper().get(user, ResponseUserDto.class));;
//        }
//        else{
//            ResponseUserDto userUpDated = customUserDetailsService.update(user);
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userUpDated);
//        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping
    public ResponseEntity<ResponseUserDto> findUser(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok().body(customUserDetailsService.findByEmail(requestUserDto.getEmail()));

//        if(!customUserDetailsService.existsById(user.getId())){ //el Id no existe?
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
//        }
//        else{
//            ResponseUserDto userFound = customUserDetailsService.findByEmail(user.getEmail());
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userFound);
//        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getUserPage(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<ResponseUserDto> users = customUserDetailsService.findAllUsersPageable(page);

            PagedModel<UserModel> model = pagedResourcesAssembler.toModel(users, userModelAssembler);

            return ResponseEntity.ok().body(model);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron elementos" + e.getMessage());
        }
    }

}
