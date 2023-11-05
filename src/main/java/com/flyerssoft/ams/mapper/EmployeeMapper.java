package com.flyerssoft.ams.mapper;

import com.flyerssoft.ams.client.microsoft.dto.Profile;
import com.flyerssoft.ams.model.dto.EmployeeDto;
import com.flyerssoft.ams.model.entity.Employee;
import com.flyerssoft.ams.security.User;
import org.mapstruct.Mapper;

/**
 * The EmployeeMapper interface is responsible for
 * mapping Employee objects to custom objects
 * and vice versa.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {
  Employee profileResponseToEmployee(Profile profileResponse);

  EmployeeDto toDto(Employee employee);

  User toUser(Employee employee);
}
