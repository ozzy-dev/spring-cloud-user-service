package com.microservice.demo.controller;

import com.microservice.demo.annotation.DefaultExceptionMessage;
import com.microservice.demo.dto.UserDTO;
import com.microservice.demo.entity.ResponseWrapper;
import com.microservice.demo.exception.TicketingProjectException;
import com.microservice.demo.service.RoleService;
import com.microservice.demo.service.UserService;
import com.microservice.demo.util.MapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller",description = "User API")
public class UserController {

//    @Value("${app.local-url}")
//    private String BASE_URL;

    private UserService userService;
    private MapperUtil mapperUtil;
    private RoleService roleService;


    public UserController(UserService userService, MapperUtil mapperUtil, RoleService roleService) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
        this.roleService = roleService;
    }

    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @PostMapping("/create-user")
    @Operation(summary = "Create new account")
    public ResponseEntity<ResponseWrapper> doRegister(@RequestBody UserDTO userDTO) throws TicketingProjectException {
        UserDTO createdUser = userService.save(userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User has been created!",createdUser));
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Read All Users")
    public ResponseEntity<ResponseWrapper> readAll(){
        List<UserDTO> result = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved users",result));
    }

    @GetMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Read by username")
    //Only admin should see other profiles or current user can see his/her profile
    public ResponseEntity<ResponseWrapper> readByUsername(@PathVariable("username") String username) throws AccessDeniedException {
        UserDTO user = userService.findByUserName(username);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved user",user));
    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Update User")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) throws TicketingProjectException, AccessDeniedException {
        UserDTO updatedUser = userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("Successfully updated",updatedUser));
    }


    @DeleteMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Delete User")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) throws TicketingProjectException {
        userService.delete(username);
        return ResponseEntity.ok(new ResponseWrapper("Successfully deleted"));
    }

    @GetMapping("/role")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Read by role")
    public ResponseEntity<ResponseWrapper> readByRole(@RequestParam String role){
        List<UserDTO> userList = userService.listAllByRole(role);
        return ResponseEntity.ok(new ResponseWrapper("Successfully read users by role",userList));
    }

}
