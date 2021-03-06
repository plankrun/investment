package com.doku.investment.controllers;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.doku.investment.dto.IndustryDto;
import com.doku.investment.dto.PropertyDto;
import com.doku.investment.dto.FormRegisterDto;
import com.doku.investment.dto.UserDetailDto;
import com.doku.investment.dto.UserDto;
import com.doku.investment.dto.UserTaxDto;
import com.doku.investment.entities.User;
import com.doku.investment.entities.UserDetail;
import com.doku.investment.entities.UserTax;
import com.doku.investment.mappers.PropertyMapper;
import com.doku.investment.mappers.UserDetailMapper;
import com.doku.investment.mappers.UserMapper;
import com.doku.investment.mappers.UserTaxMapper;
import com.doku.investment.repositories.PropertyRepository;
import com.doku.investment.repositories.UserDetailRepository;
import com.doku.investment.repositories.UserRepository;
import com.doku.investment.repositories.UserTaxRepository;
import com.doku.investment.services.IndustryServices;



/**
 * @author Laurence
 * @see Controller, GetMapping, PostMapping
 * <p>
 * Controller For Handle User Request
 * <p>
 * Anotation Controller is for create a Mapping of model object and find a view
 * <p>
 * Anotation Autowired is This annotation allows Spring to resolve and inject collaborating beans into your bean
 * This anotation related to anotation Service from package service.impl
 * <p>
 * Anotation GetMapping is the newer annotation
 * for RequestMapping(method = RequestMethod.GET), used to map web requests onto specific handler classes and/or handler methods.
 * <p>
 * Anotation PostMapping is the newer annotation
 * for RequestMapping(method = RequestMethod.POST), used to map web requests onto specific handler classes and/or handler methods.
 */
@Controller
@Transactional
public class UserController {
    
	@Autowired 
	IndustryServices industryServices;
	
	@Autowired 
	PropertyRepository propertyRepository;

	@Autowired 
	UserRepository userRepository;

	@Autowired 
	UserDetailRepository userDetailRepository;

	@Autowired 
	UserTaxRepository userTaxRepository;
	
	/**
	 * @author Laurence
	 * @see GetMapping
	 * <p>
	 * Endpoint for http://localhost:8080
	 * <p>
	 * This is controller to process request to index.html
	 * <p>
	 * @param model is for link/transfer java object to html/html to java object, param in method addAttribute is model in html (left) and java model (right)  
	 * <p>
	 * We need to declare register first before go to register.html because in that html we used RegisterDto,
	 * so we need declare first in controller.
	 * <p>
	 * @return index go to index.html
	 */
    @GetMapping("/")
    public String showLogin(Model model) {
    	    	
    	model.addAttribute("user", new UserDto());
    	
    	return "index";
    }    
    
	/**
	 * @author Laurence
	 * @see GetMapping, Valid, ModelAttribute
	 * <p>
	 * Endpoint for http://localhost:8080/signup
	 * <p>
	 * This is controller to process request to register.html
	 * <p>
	 * @param model is for link/transfer java object to html/html to java object, param in method addAttribute is model in html (left) and java model (right)  
	 * <p>
	 * propertiesDto this is example how to use MapperStruct.
	 * <p>
	 * We need to declare register first before go to register.html because in that html we used RegisterDto,
	 * so we need declare first in controller.
	 * <p>
	 * @return register go to register.html
	 */
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
    	
    	List<IndustryDto> industries = industryServices.getListIndustry();    	
    	List<PropertyDto> propertiesDto = PropertyMapper.INSTANCE.toListProperty(propertyRepository.findAll());
    	    	
    	model.addAttribute("industries", industries);
    	model.addAttribute("properties", propertiesDto);
    	model.addAttribute("formRegister", new FormRegisterDto());
    	
        return "register";
    }    
    
    /**
	 * @author Laurence
	 * @see PostMapping, Valid, ModelAttribute
	 * <p>
	 * Endpoint for http://localhost:8080/adduser
	 * <p>
	 * This is controller to process request to register.html
	 * <p>
	 * Anotation Valid is for validation attribute in RegisterDto, reference to anotation NotBlank, NotEmpty, etc 
	 * <p>
	 * Anotation ModelAttribute is for mapping data register from html to java object (DTO)
	 * <p>
	 * @param formRegister is getting value from register.html  
	 * <p>
	 * @param result is for validation if input from user is not what we expected, can reference to Anotation Valid
	 * <p>
	 * @param model is for link/transfer java object to html/html to java object, param in method addAttribute is model in html (left) and java model (right)
	 * <p>
	 * @return index go to index.html if input or result no error
	 * <p>
	 * @return register go to error.html if input or result has error
	 */
    @PostMapping("/adduser")
    	public String addUser(@Valid @ModelAttribute FormRegisterDto formRegister, BindingResult result, Model model) {
    	if (result.hasErrors()) {
          return "redirect:error";
    	}else {
    		
    		User user = UserMapper.INSTANCE.userDtoToUser(formRegister.getUserDto());
    		UserDetail userDetail = UserDetailMapper.INSTANCE.userDetailDtoToUserDetail(formRegister.getUserDetailDto());
    		UserTax userTax = UserTaxMapper.INSTANCE.userTaxDtoToUserTax(formRegister.getUserTaxDto());

    		userRepository.save(user);
    		userDetail.setUserId(user.getId());
    		userDetailRepository.save(userDetail);
    		
    		userTax.setId(userTaxRepository.getNextVal());
    		userTax.setUserDetailId(userDetail.getId());
    		userTaxRepository.save(userTax);
    		
            return "index"; 
    	}
    }
    
    /**
	 * @author Laurence
	 * @see PostMapping, Valid, ModelAttribute
	 * <p>
	 * Endpoint for http://localhost:8080/profile
	 * <p>
	 * This is controller to process request to profile.html, process registration use
	 * <p>
	 * Anotation Valid is for validation attribute in RegisterDto, reference to anotation NotBlank, NotEmpty, etc 
	 * <p>
	 * Anotation ModelAttribute is for mapping data register from html to java object (DTO)
	 * <p>
	 * This is controller to process registration user
	 * <p>
	 * @param formRegister is getting value from register.html  
	 * <p>
	 * @param result is for validation if input from user is not what we expected, can reference to Anotation Valid
	 * <p>
	 * @param model is for link/transfer java object to html/html to java object, param in method addAttribute is model in html (left) and java model (right)
	 * <p>
	 * @return index go to index.html if input or result no error
	 * <p>
	 * @return register go to error.html if input or result has error
	 */
    @PostMapping("/profile")
    public String profile(@Valid @ModelAttribute UserDto user, BindingResult result, Model model) {
    	
    	Integer idUser = userRepository.getIdFindByUsernameAndPassword(user.getUsername(), user.getPassword());
    	
    	UserDetailDto profile = UserDetailMapper.INSTANCE.userDetailToUserDetailDto(userDetailRepository.findByUserId(idUser));
    	
    	model.addAttribute("profile", profile);
        return "profile";
    }
    
    
    
    
    
    
    
    
    
    
    
//    @PostMapping("/adduser")
//    public String addUser(@Valid UserDto user, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "register";
//        }
//        
////        userRepository.save(user);
//        model.addAttribute("users", userRepository.findAll());
//        return "index";
//    }
    
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
//        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//        model.addAttribute("user", user);
        return "update-user";
    }
    
//    @PostMapping("/update/{id}")
//    public String updateUser(@PathVariable("id") int id, @Valid User user, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            user.setId(id);
//            return "update-user";
//        }
//        
//        userRepository.save(user);
//        model.addAttribute("users", userRepository.findAll());
//        return "index";
//    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
//        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//        userRepository.delete(user);
//        model.addAttribute("users", userRepository.findAll());
        return "index";
    }
}