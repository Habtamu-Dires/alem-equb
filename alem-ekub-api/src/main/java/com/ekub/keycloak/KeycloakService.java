package com.ekub.keycloak;

import com.ekub.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Autowired
    private KeycloakClient keycloakClient;

    private final RestTemplate restTemplate = new RestTemplate();

    // create users
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createUser(KeycloakUserRequest userRequest) {
        String url = keycloakAuthUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(keycloakClient.getClientAccessToken());

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", userRequest.username());
        payload.put("firstName", userRequest.firstName());
        payload.put("lastName", userRequest.lastName());
        payload.put("email", userRequest.email());
        payload.put("enabled", userRequest.enabled());
        payload.put("credentials", List.of(Map.of(
                "type", "password",
                "value", userRequest.password(),
                "temporary", true
        )));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

        if(response.getStatusCode() == HttpStatus.CREATED){
            String locationHeader = response.getHeaders().getLocation().toString();
            return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
        } else {
            throw new RuntimeException("Failed to create user in Keycloak");
        }

    }


    // get all users
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserResponse> getAllUsers() {
        String url = keycloakAuthUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(keycloakClient.getClientAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                List.class
        );

        List<?> body = response.getBody();
        System.out.println(body);

        List<UserResponse> userResponseList = body.stream()
                .map(map -> (HashMap<String, Object>) map)
                .peek(map -> {
                    System.out.println("name " + map.get("username") + " id " + map.get("id"));
                })
                .map(map -> UserResponse.builder()
                        .username(String.valueOf(map.get("username")))
                        .firstname(String.valueOf(map.get("firstName")))
                        .lastname(String.valueOf(map.get("lastName")))
                        .email(String.valueOf(map.get("email")))
                        .build()
                ).toList();
        return userResponseList;
    }

    // update user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateUser(String userId, KeycloakUserRequest userRequest) {
        String url = keycloakAuthUrl + "/admin/realms/" + realm + "/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(keycloakClient.getClientAccessToken());

        Map<String, Object> payload = new HashMap<>();
        payload.put("firstName", userRequest.firstName());
        payload.put("lastName", userRequest.lastName());
        payload.put("email", userRequest.email());
        payload.put("enabled", userRequest.enabled());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
    }

    // update profile
    public void updateProfile(String userId, KeycloakUserRequest userRequest) {
        String url = keycloakAuthUrl + "/admin/realms/" + realm + "/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(keycloakClient.getClientAccessToken());

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", userRequest.email());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
    }

    // delete user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(String userId) {
        String url = keycloakAuthUrl + "/admin/realms/" + realm + "/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(keycloakClient.getClientAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
    }

    // update password
    public void updatePassword(String userId, String newPassword) {
        String url = keycloakAuthUrl +"/admin/realms/"+realm+"/users/" + userId + "/reset-password";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(keycloakClient.getClientAccessToken());

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "password");
        payload.put("temporary", false);
        payload.put("value", newPassword);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Void.class
        );

        if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
            throw new RuntimeException("Failed to update password");
        }
    }



    // login and get access token
    public String login(String username, String oldPassword) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenEndpoint = keycloakAuthUrl +"/realms/"+realm+"/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", oldPassword);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> res = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            return String.valueOf(res.getBody().get("access_token"));
        } catch (Exception ex) {
            throw new AccessDeniedException("Login Failed");
        }
    }

    public void logoutUserFromAllDevices(String userId) {
        String logoutUrl = keycloakAuthUrl+"/admin/realms/"+realm+"/users/"+userId+"/logout";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(keycloakClient.getClientAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            restTemplate.postForEntity(logoutUrl, request, Void.class);
            System.out.println("User logged out from all devices.");
        } catch (Exception ex) {
            System.err.println("Failed to logout user: " + ex.getMessage());
        }
    }
}
