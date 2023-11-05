package com.flyerssoft.ams.model.dto;

import java.util.List;

/**
 * This record will set transfer object on successful login.
 *
 * @param profile The profile associated with the login response.
 * @param expiresIn The expiration time of the access token.
 * @param accessToken The access token.
 */
public record LoginResponse(
    EmployeeDto profile,
    long expiresIn,
    String accessToken,
    List<String> userGroupPermissions,
    List<EntitlementDto> entitlements
) {}
