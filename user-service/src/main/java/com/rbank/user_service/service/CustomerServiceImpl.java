package com.rbank.user_service.service;

import com.rbank.user_service.dto.CustomerDTO;
import com.rbank.user_service.dto.CustomerAuthDTO;
import com.rbank.user_service.dto.ResponseDTO;
import com.rbank.user_service.enums.RoleType;
import com.rbank.user_service.exception.CustomerAlreadyExistException;
import com.rbank.user_service.exception.CustomerNotFoundException;
import com.rbank.user_service.exception.ResourceNotFoundException;
import com.rbank.user_service.model.Customer;
import com.rbank.user_service.model.Role;
import com.rbank.user_service.repository.CustomerRepository;
import com.rbank.user_service.repository.RoleRepository;
import com.rbank.user_service.utils.CustomerMapper;
import com.rbank.user_service.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResponseUtils responseUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public ResponseEntity<ResponseDTO> handleRegisterCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException,ResourceNotFoundException{
        LOGGER.info("Get input data for creating Customer name: {}", customerDTO.getFirstName());
        Optional<Customer> existingCustomer = customerRepository.findByMobile(customerDTO.getMobile());
        if(existingCustomer.isPresent()){
            throw new CustomerAlreadyExistException("Customer already found with id: " + existingCustomer.get().getCustomerId());
        }
        Customer customer = CustomerMapper.mapDTOToEntity(customerDTO);
        Set<Role> roles = new HashSet<>();
        if(customerDTO.getRoles() == null){
            Role role = roleRepository.findByRoleType(RoleType.USER)
                    .orElseThrow(()-> new ResourceNotFoundException("Role is not found: USER"));
            roles.add(role);
        }else {
            for(RoleType roleType : customerDTO.getRoles()){
                Role role = roleRepository.findByRoleType(roleType)
                        .orElseThrow(()-> new ResourceNotFoundException("Role is not found: " + roleType));
                roles.add(role);
            }
        }
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setRoles(roles);
        Customer registeredCustomer = customerRepository.save(customer);
        LOGGER.info("Customer created with  id: {}, name: {}",registeredCustomer.getCustomerId(), registeredCustomer.getFirstName());
        return responseUtils.handleResponseInPayload(registeredCustomer,"Customer register Successfully",201, HttpStatus.CREATED);
    }

    @Override
    public Customer getCustomerById(Long id) throws CustomerNotFoundException{
        Optional<Customer> existCustomer = customerRepository.findById(id);
        if(existCustomer.isEmpty()){
            throw new CustomerNotFoundException("Customer not found" + id);
        }
        return existCustomer.get();
    }


    @Override
    public CustomerAuthDTO handleGetCustomerWithRoles(String username) throws CustomerNotFoundException{
        LOGGER.info("Fetching Customer details with username: {}",username);
        Optional<Customer> existCustomer = customerRepository.findByUsernameWithRoles(username);
        if(existCustomer.isEmpty()){
            LOGGER.error("Customer doesn't exist Fetching Customer details with username : {}",username);
            throw new CustomerNotFoundException("Customer does not exist");
        }
        LOGGER.info("After fetching Customer details with username then CustomerId is : {}",existCustomer.get().getCustomerId());
        return new CustomerAuthDTO(existCustomer.get());
    }
}
