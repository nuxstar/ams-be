package com.flyerssoft.ams.service.impl;

import com.flyerssoft.ams.mapper.EmployeeMapper;
import com.flyerssoft.ams.model.repository.EmployeeRepository;
import com.flyerssoft.ams.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The implementation of the EmployeeService interface.
 * This class provides methods for adding employees and
 * interacting with the EmployeeRepository.
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

  /**
   * The EmployeeMapper used for mapping Profile objects
   * to Employee objects.
   */
  @Autowired
  private EmployeeMapper employeeMapper;

  /**
   * The EmployeeRepository used for accessing and persisting Employee objects.
   */
  @Autowired
  private EmployeeRepository employeeRepository;
}
