package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.CustomUserDetailsService;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/users")
@ApiModel("Controlador de usuario")
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUserDto> updateUser(@RequestBody ResponseUserDto user) {
        if(!customUserDetailsService.existsById(user.getId())){ //el Id no existe? o no es correcto?
            System.out.println("Trying to update User without Id");
            return ResponseEntity.badRequest().body(user);
        }
        else{
            ResponseUserDto userUpDated = customUserDetailsService.update(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userUpDated);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> findUser(@RequestBody ResponseUserDto user) {
        if(!customUserDetailsService.existsById(user.getId())){ //el Id no existe?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        else{
            ResponseUserDto userFound = customUserDetailsService.findByEmail(user.getEmail());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userFound);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<String> findAllUsers(@RequestParam Map<String, Object> params, Model model) {
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) -1) : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<ResponseUserDto> userDtoPage = customUserDetailsService.findAllPageable(pageRequest);

        int totalPages = userDtoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        model.addAttribute("titulo", "Listado Usuarios");
        model.addAttribute("users", userDtoPage.getContent());

        return ResponseEntity.ok("all");
    }

}
