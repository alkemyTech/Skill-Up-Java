package com.alkemy.wallet.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.alkemy.wallet.dto.PatchRequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { UserController.class })
@ContextConfiguration(classes = { UserController.class })
class UserControllerTest {
	private String uri = "/users";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userService;

	private ObjectMapper objectMapper = new ObjectMapper();


	@Test
	void findAllUsers_GetRequest_ResponseOk() throws Exception {
		when(userService.findAllUsers())
			.thenReturn(new ArrayList<ResponseUserDto>());
		
		mockMvc
			.perform(get(uri))
			.andExpect(status().isOk());
	}


	@Test
	void getUserDetails_GetRequestWithUriWithVariable_ResponseOk()
		throws Exception {
		Long userId = 1L;
		String token = "token";

		mockMvc
			.perform(
				get(uri + "/" + userId.toString())
					.header("authorization", "Bearer " + token))
			.andExpect(status().isOk());
	}


	@Test
	void updateUserDetails_PatchRequestWithBodyAndUriWithId_ResponseOk()
		throws Exception {
		Long userId = 1L;
		String token = "token";
		ResponseUserDto dto = new ResponseUserDto();

		when(
			userService
				.updateUserDetails(
					any(),
					any(),
					any()
					)
				)
			.thenReturn(dto);

		String body =
			objectMapper
				.writeValueAsString(new PatchRequestUserDto());

		mockMvc
			.perform(
				patch(uri + "/" + userId.toString())
					.content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.header("authorization", "Bearer " + token))
			.andExpect(status().isOk());

	}

}
