package com.flyerssoft.ams.mapper;

import com.flyerssoft.ams.model.dto.EntitlementDto;
import com.flyerssoft.ams.model.entity.Entitlement;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * The EntitlementMapper interface is responsible for
 * mapping Entitlement objects to custom objects
 * and vice versa.
 */
@Mapper(componentModel = "spring")
public interface EntitlementMapper {
  List<EntitlementDto> toDto(List<Entitlement> entitlements);
}
