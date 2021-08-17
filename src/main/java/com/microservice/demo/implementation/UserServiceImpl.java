package com.microservice.demo.implementation;

import com.microservice.demo.dto.UserDTO;
import com.microservice.demo.entity.User;
import com.microservice.demo.exception.TicketingProjectException;
import com.microservice.demo.repository.UserRepository;
import com.microservice.demo.service.UserService;
import com.microservice.demo.util.MapperUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private MapperUtil mapperUtil;


    public UserServiceImpl(UserRepository userRepository,MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> list = userRepository.findAll(Sort.by("firstName"));
        return list.stream().map(obj -> mapperUtil.convert(obj,new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) throws AccessDeniedException {
        User user = userRepository.findByUserName(username);
        return mapperUtil.convert(user,new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO dto) throws TicketingProjectException {

        User foundUser = userRepository.findByUserName(dto.getUserName());

        if(foundUser!=null){
            throw new TicketingProjectException("User already exists");
        }

        User user =  mapperUtil.convert(dto,new User());


        User save = userRepository.save(user);

        return mapperUtil.convert(save,new UserDTO());

    }

    @Override
    public UserDTO update(UserDTO dto) throws TicketingProjectException, AccessDeniedException {

        //Find current user
        User user = userRepository.findByUserName(dto.getUserName());

        if(user == null){
            throw new TicketingProjectException("User Does Not Exists");
        }
        //Map update user dto to entity object
        User convertedUser = mapperUtil.convert(dto,new User());

        if(!user.getEnabled()){
            throw new TicketingProjectException("User is not confirmed");
        }

        convertedUser.setEnabled(true);

        //set id to the converted object
        convertedUser.setId(user.getId());
        //save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    @Override
    public void delete(String username) throws TicketingProjectException {
        User user = userRepository.findByUserName(username);

        if(user == null){
            throw new TicketingProjectException("User Does Not Exists");
        }

        user.setUserName(user.getUserName() + "-" + user.getId());

        user.setIsDeleted(true);
        userRepository.save(user);
    }

    //hard delete
    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }


    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return users.stream().map(obj -> {return mapperUtil.convert(obj,new UserDTO());}).collect(Collectors.toList());
    }


    @Override
    public UserDTO confirm(User user) {

        user.setEnabled(true);
        User confirmedUser = userRepository.save(user);

        return mapperUtil.convert(confirmedUser,new UserDTO());
    }

}

