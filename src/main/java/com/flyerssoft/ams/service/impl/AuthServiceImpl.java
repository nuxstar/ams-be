package com.flyerssoft.ams.service.impl;

import com.flyerssoft.ams.client.microsoft.auth.MicrosoftAuthClient;
import com.flyerssoft.ams.client.microsoft.dto.Profile;
import com.flyerssoft.ams.client.microsoft.dto.TokenResponse;
import com.flyerssoft.ams.client.microsoft.graph.MicrosoftGraphClient;
import com.flyerssoft.ams.exception.BadRequestException;
import com.flyerssoft.ams.exception.NotFoundException;
import com.flyerssoft.ams.mapper.EmployeeMapper;
import com.flyerssoft.ams.mapper.EntitlementMapper;
import com.flyerssoft.ams.model.dto.EmployeeDto;
import com.flyerssoft.ams.model.dto.EntitlementDto;
import com.flyerssoft.ams.model.dto.LoginResponse;
import com.flyerssoft.ams.model.entity.Employee;
import com.flyerssoft.ams.model.entity.Entitlement;
import com.flyerssoft.ams.model.entity.UserGroupPermission;
import com.flyerssoft.ams.model.repository.EmployeeRepository;
import com.flyerssoft.ams.model.repository.EntitlementRepository;
import com.flyerssoft.ams.model.repository.UserGroupPermissionRepository;
import com.flyerssoft.ams.security.JwtService;
import com.flyerssoft.ams.service.AuthService;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation class for the AuthService interface.
 */
@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private MicrosoftAuthClient microsoftAuthClient;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EntitlementRepository entitlementRepository;

  @Autowired
  private UserGroupPermissionRepository userGroupPermissionRepository;

  @Autowired
  private EntitlementMapper entitlementMapper;

  @Autowired
  EmployeeMapper employeeMapper;

  @Autowired
  private JwtService jwtService;

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  /**
   * Microsoft Tenant I'd which is present in application properties.
   */
  @Value("${api.microsoft.sso.tenant.id}")
  private String tenantId;

  /**
   * Microsoft Client I'd which is present in application properties.
   */
  @Value("${api.microsoft.sso.client.id}")
  private String clientId;

  /**
   * Microsoft Client secret which is present in application properties.
   */
  @Value("${api.microsoft.sso.client.secret}")
  private String clientSecret;

  /**
   * Microsoft Redirect URL which is present in application properties.
   */
  @Value("${api.microsoft.sso.redirectUri}")
  private String redirectUri;

  /**
   * Microsoft Scopes for our api to consume
   * which is present in application properties.
   */
  @Value("${api.microsoft.sso.scopes}")
  private String scopes;

  /**
   * Grant type holds the flow in which we want to achieve
   * microsoft authentication.
   */
  @Value("${api.microsoft.sso.grantType}")
  private String grantType;

  @Value("${super-admin.password}")
  private String superAdminPassword;

  /**
   * Holds the end points Microsoft Graph client.
   */
  @Autowired
  private MicrosoftGraphClient microsoftGraphClient;

  private String parseCredentials(String authorizationHeader) {
    if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
      throw new BadRequestException("Invalid Credentials");
    }
    String encodedCredentials = authorizationHeader.substring(6).trim();
    byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
    return new String(decodedBytes);
  }

  private Map<String, Object> getClaimsForSuperAdmin(
      Employee superAdmin,
      List<UserGroupPermission> userGroupPermissions,
      List<Entitlement> entitlements
  ) {
    Map<String, Object> customClaims = new HashMap<>();
    customClaims.put("iss", "Flyerssoft_AMS");
    customClaims.put("name", superAdmin.getEmployeeName());
    customClaims.put("email", superAdmin.getEmployeeEmail());

    Set<String> allEntitlements = entitlements
        .stream()
        .map(Entitlement::getName)
        .collect(Collectors.toSet());
    customClaims.put(
        "user_permissions",
        allEntitlements
    );
    List<String> allGroups = userGroupPermissions
        .stream()
        .map(UserGroupPermission::getName)
        .toList();
    customClaims.put(
        "groups",
        allGroups
    );
    return customClaims;
  }

  /**
   * Authenticates the user based on the provided authorization code.
   *
   * @param authCode The authorization code obtained from the
   *                 authentication process.
   * @return A LoginResponseDto object containing the profile information
   *         and access token.
   */
  public LoginResponse authenticate(
      final String authCode
  ) {

    Map<String, String> urlEncodedFormData = new HashMap<>();

    urlEncodedFormData.put("code", authCode);
    urlEncodedFormData.put("client_id", clientId);
    urlEncodedFormData.put("redirect_uri", redirectUri);
    urlEncodedFormData.put("grant_type", grantType);
    urlEncodedFormData.put("client_secret", clientSecret);

    TokenResponse tokenResponse = microsoftAuthClient.getAccessToken(tenantId, urlEncodedFormData);

    String bearerToken = "Bearer " + tokenResponse.accessToken();

    Profile profileResponse = microsoftGraphClient.getUserById(bearerToken);

    return null;
  }

  /**
   * generate login token for super admin.
   *
   * @param authorizationHeader authorization header contains basic auth.
   * @return LoginResponseDto contains token and it's expiry time.
   * @throws ParseException Parse Exception
   */
  @Override
  public LoginResponse login(String authorizationHeader) throws ParseException {
    String decodedCredentials = parseCredentials(authorizationHeader);

    // Split the decoded credentials into username and password
    String[] credentials = decodedCredentials.split(":", 2);
    String userEmail = credentials[0];
    String password = credentials[1];

    Employee superAdmin = employeeRepository
        .findById(1)
        .orElseThrow(() -> new NotFoundException(
            String.format(NotFoundException.USER_NOT_FOUND, 1)));
    var superAdminUser = employeeMapper.toUser(superAdmin);

    if (!superAdmin.getEmployeeEmail().equals(userEmail)) {
      throw new BadRequestException("Invalid Credentials");
    }

    if (!passwordEncoder.matches(password, superAdminPassword)) {
      throw new BadRequestException("Invalid Credentials");
    }

    List<UserGroupPermission> groupPermissions = userGroupPermissionRepository.findAll();
    List<Entitlement> entitlements = entitlementRepository.findAll();

    Map<String, Object> customClaims = getClaimsForSuperAdmin(
        superAdmin,
        groupPermissions,
        entitlements
    );

    String token = jwtService.generateToken(customClaims, superAdminUser);
    Date expirationTime = jwtService.extractExpiration(token);

    List<EntitlementDto> entitlementDtos = entitlementMapper.toDto(entitlements);
    EmployeeDto superAdminDto = employeeMapper.toDto(superAdmin);
    return new LoginResponse(
        superAdminDto,
        expirationTime.getTime(),
        token,
        (List<String>) customClaims.get("groups"),
        entitlementDtos
    );
  }

}
