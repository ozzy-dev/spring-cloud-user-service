package com.microservice.demo.service;

import com.microservice.demo.dto.RoleDTO;
import com.microservice.demo.exception.TicketingProjectException;

import java.util.List;

public interface RoleService {

    List<RoleDTO> listAllRoles();
    RoleDTO findById(Long id) throws TicketingProjectException;
}
