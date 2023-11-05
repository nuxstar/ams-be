package com.flyerssoft.ams.model.dto;

import jakarta.persistence.Column;

/**
 * Represents an employee data transfer object (DTO).
 *
 * @param name           Entitlement Name.
 * @param allowedMethod         http method.
 * @param pathPattern        Url pattern.
 */
public record EntitlementDto(
    String name,
    String allowedMethod,
    String pathPattern
) {
}
