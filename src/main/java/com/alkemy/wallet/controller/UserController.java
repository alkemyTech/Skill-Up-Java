package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.UserModelAssembler;
import com.alkemy.wallet.assembler.model.UserModel;
import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.interfaces.ICustomUserDetailsService;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@ApiModel("Controlador de usuario")
public class UserController {

    @Autowired
    private ICustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserModelAssembler userModelAssembler;
    @Autowired
    private PagedResourcesAssembler<ResponseUserDto> pagedResourcesAssembler;


    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUserDto> updateUser(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customUserDetailsService.update(requestUserDto));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getUserLoggedDetails(@PathVariable Long id) {
        return ResponseEntity.ok().body(customUserDetailsService.getUserLoggedById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getUserPage(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<ResponseUserDto> users = customUserDetailsService.findAllUsersPageable(page);
            PagedModel<UserModel> model = pagedResourcesAssembler.toModel(users, userModelAssembler);
            return ResponseEntity.ok().body(model);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty list" + e.getMessage());
        }
    }

}
