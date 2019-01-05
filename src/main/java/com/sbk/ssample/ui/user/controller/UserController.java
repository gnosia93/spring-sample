package com.sbk.ssample.ui.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbk.ssample.app.service.user.UserService;
import com.sbk.ssample.app.service.user.command.AddUserCommand;
import com.sbk.ssample.ui.user.request.AddUserRequest;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/user")
public class UserController implements UserControllerMapper {

	final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
		
		AddUserRequest userDto = new AddUserRequest();
		model.addAttribute("userForm", userDto);
		
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("userForm") @Valid AddUserRequest addUserRequest,
			BindingResult bindingResult) {
	
		if(bindingResult.hasErrors()) {

			return "registration";
		};
		
		AddUserCommand addUserCommand = asAddUserCommand(addUserRequest);
		userService.add(addUserCommand);
		
		return "redirect:login";
	}
	
	@GetMapping("/existUser/{id}")
	public void existUser(@PathVariable String id) {
		
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
}
