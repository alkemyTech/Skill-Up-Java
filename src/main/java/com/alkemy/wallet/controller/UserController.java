package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@ApiModel("Controlador de usuario")
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
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

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers(Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(customUserDetailsService.findAllPageable(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron elementos" + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {

        String usuarioId = jwtUtil.getKey(token);
        if (usuarioId == null) {
            return null;
        }
        customUserDetailsService.softDelete(id);

        return ResponseEntity.ok(Boolean.TRUE);

    }

}
