package com.microservice.demo.controller;


import com.microservice.demo.annotation.DefaultExceptionMessage;
import com.microservice.demo.dto.RoleDTO;
import com.microservice.demo.entity.ResponseWrapper;
import com.microservice.demo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@Tag(name = "Role Controller",description = "Role API")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong,please try again!")
    @Operation(summary = "Read all roles")
    public ResponseEntity<ResponseWrapper> readAll(){
        List<RoleDTO> roleDTOList = roleService.listAllRoles();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all roles",roleDTOList));

    }


}
