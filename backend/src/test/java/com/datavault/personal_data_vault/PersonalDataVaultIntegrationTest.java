package com.datavault.personal_data_vault;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.datavault.personal_data_vault.entity.PersonalData;
import com.datavault.personal_data_vault.repository.PersonalDataRepository;
import com.datavault.personal_data_vault.repository.UserRepository;
import java.util.UUID;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PersonalDataVaultIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonalDataRepository personalDataRepository;
    @Autowired
    private UserRepository userRepository;

    PersonalDataVaultIntegrationTest() {
    }

    @Test
    void registrationLoginVaultOwnershipAndEncryptionWorkEndToEnd() throws Exception {
        RegisteredUser user = this.registerUser();
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/auth/me", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(user.accessToken())})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.email", (Matcher)Matchers.is((Object)user.email()))).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.fullName", (Matcher)Matchers.is((Object)"Vault User")));
        String refreshResponse = this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/auth/refresh", (Object[])new Object[0]).contentType(MediaType.APPLICATION_JSON).content("{\"refreshToken\":\"" + user.refreshToken() + "\"}")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        String rotatedRefreshToken = this.objectMapper.readTree(refreshResponse).path("data").path("refreshToken").asText();
        Assertions.assertNotEquals((Object)user.refreshToken(), (Object)rotatedRefreshToken);
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/auth/refresh", (Object[])new Object[0]).contentType(MediaType.APPLICATION_JSON).content("{\"refreshToken\":\"" + user.refreshToken() + "\"}")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/vault/data", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(user.accessToken())}).contentType(MediaType.APPLICATION_JSON).content("{\"dataType\":\"EMAIL\",\"value\":\"private@example.test\",\"description\":\"Primary email\"}")).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.value", (Matcher)Matchers.is((Object)"private@example.test")));
        PersonalData stored = (PersonalData)this.personalDataRepository.findByUserIdAndDataType(Long.valueOf(user.userId()), PersonalData.DataType.EMAIL).orElseThrow();
        Assertions.assertNotEquals((Object)"private@example.test", (Object)stored.getEncryptedValue());
        Assertions.assertFalse((boolean)stored.getEncryptedValue().contains("private@example.test"));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.patch((String)"/api/vault/data/{id}", (Object[])new Object[]{stored.getId()}).header("Authorization", new Object[]{this.bearer(user.accessToken())}).contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Updated description\"}")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.value", (Matcher)Matchers.is((Object)"private@example.test"))).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.description", (Matcher)Matchers.is((Object)"Updated description")));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/vault/data", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(user.accessToken())})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data[0].value", (Matcher)Matchers.is((Object)"private@example.test")));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/auth/me", (Object[])new Object[0])).andExpect(MockMvcResultMatchers.status().isUnauthorized());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/admin/users", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(user.accessToken())})).andExpect(MockMvcResultMatchers.status().isForbidden());
        String login = "{\"email\":\"%s\",\"password\":\"StrongPass!7\"}\n".formatted(user.email());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/auth/login", (Object[])new Object[0]).contentType(MediaType.APPLICATION_JSON).content(login)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.success", (Matcher)Matchers.is((Object)true)));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/auth/logout", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(user.accessToken())}).contentType(MediaType.APPLICATION_JSON).content("{\"refreshToken\":\"" + rotatedRefreshToken + "\"}")).andExpect(MockMvcResultMatchers.status().isOk());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/auth/refresh", (Object[])new Object[0]).contentType(MediaType.APPLICATION_JSON).content("{\"refreshToken\":\"" + rotatedRefreshToken + "\"}")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
        Assertions.assertTrue(this.userRepository.findById(Long.valueOf(user.userId())).isPresent());
    }

    @Test
    void applicationCanOnlyReadMatchingConsentedDataAndAttemptsAreAudited() throws Exception {
        RegisteredUser user = this.registerUser();
        String userHeader = this.bearer(user.accessToken());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/vault/data", (Object[])new Object[0]).header("Authorization", new Object[]{userHeader}).contentType(MediaType.APPLICATION_JSON).content("{\"dataType\":\"EMAIL\",\"value\":\"private@example.test\"}")).andExpect(MockMvcResultMatchers.status().isCreated());
        String appResponse = this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/apps", (Object[])new Object[0]).header("Authorization", new Object[]{userHeader}).contentType(MediaType.APPLICATION_JSON).content("{\"applicationName\":\"Shopping-" + String.valueOf(UUID.randomUUID()) + "\",\"description\":\"Order notifications\",\"redirectUri\":\"https://shop.example.test/callback\"}")).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        JsonNode app = this.objectMapper.readTree(appResponse).path("data");
        long appId = app.path("id").asLong();
        String appTokenResponse = this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/apps/token", (Object[])new Object[0]).contentType(MediaType.APPLICATION_JSON).content("{\"clientId\":\"" + app.path("clientId").asText() + "\",\"clientSecret\":\"" + app.path("clientSecret").asText() + "\"}")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        String appToken = this.objectMapper.readTree(appTokenResponse).path("data").path("accessToken").asText();
        String consentResponse = this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/consents/request", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(appToken)}).contentType(MediaType.APPLICATION_JSON).content("{\"userId\":" + user.userId() + ",\"applicationId\":" + appId + ",\"dataType\":\"EMAIL\",\"purpose\":\"Order Confirmation\",\"operation\":\"READ\",\"requestedDurationDays\":7}")).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        long consentId = this.objectMapper.readTree(consentResponse).path("data").path("id").asLong();
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.put((String)"/api/consents/{id}/grant", (Object[])new Object[]{consentId}).header("Authorization", new Object[]{userHeader})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.status", (Matcher)Matchers.is((Object)"GRANTED")));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/data-access/EMAIL", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(appToken)}).param("userId", new String[]{Long.toString(user.userId())}).param("purpose", new String[]{"Order Confirmation"})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.value", (Matcher)Matchers.is((Object)"private@example.test")));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/data-access/PHONE", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(appToken)}).param("userId", new String[]{Long.toString(user.userId())}).param("purpose", new String[]{"Order Confirmation"})).andExpect(MockMvcResultMatchers.status().isForbidden());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/data-access/EMAIL", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(appToken)}).param("userId", new String[]{Long.toString(user.userId())}).param("purpose", new String[]{"Marketing"})).andExpect(MockMvcResultMatchers.status().isForbidden());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.put((String)"/api/data-access/EMAIL", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(appToken)}).contentType(MediaType.APPLICATION_JSON).content("{\"userId\":" + user.userId() + ",\"purpose\":\"Order Confirmation\",\"value\":\"overwrite\"}")).andExpect(MockMvcResultMatchers.status().isForbidden());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/audit/logs", (Object[])new Object[0]).header("Authorization", new Object[]{userHeader})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.length()", (Matcher)Matchers.is((Object)4)));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.put((String)"/api/consents/{id}/revoke", (Object[])new Object[]{consentId}).header("Authorization", new Object[]{userHeader})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.status", (Matcher)Matchers.is((Object)"REVOKED")));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/data-access/EMAIL", (Object[])new Object[0]).header("Authorization", new Object[]{this.bearer(appToken)}).param("userId", new String[]{Long.toString(user.userId())}).param("purpose", new String[]{"Order Confirmation"})).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void privateRoutesRequireBearerAuthentication() throws Exception {
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/vault/data", (Object[])new Object[0])).andExpect(MockMvcResultMatchers.status().isUnauthorized()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.errorCode", (Matcher)Matchers.is((Object)"AUTHENTICATION_REQUIRED")));
    }

    @Test
    void vaultCrudIsOwnerScoped() throws Exception {
        RegisteredUser owner = this.registerUser();
        RegisteredUser otherUser = this.registerUser();
        String ownerHeader = this.bearer(owner.accessToken());
        String otherHeader = this.bearer(otherUser.accessToken());
        String created = this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/vault/data", (Object[])new Object[0]).header("Authorization", new Object[]{ownerHeader}).contentType(MediaType.APPLICATION_JSON).content("{\"dataType\":\"EMAIL\",\"value\":\"owner@example.test\"}")).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        long dataId = this.objectMapper.readTree(created).path("data").path("id").asLong();
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{ownerHeader})).andExpect(MockMvcResultMatchers.status().isOk());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.put((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{ownerHeader}).contentType(MediaType.APPLICATION_JSON).content("{\"dataType\":\"EMAIL\",\"value\":\"updated@example.test\"}")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.data.value", (Matcher)Matchers.is((Object)"updated@example.test")));
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{otherHeader})).andExpect(MockMvcResultMatchers.status().isNotFound());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.put((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{otherHeader}).contentType(MediaType.APPLICATION_JSON).content("{\"dataType\":\"EMAIL\",\"value\":\"stolen@example.test\"}")).andExpect(MockMvcResultMatchers.status().isNotFound());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.patch((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{otherHeader}).contentType(MediaType.APPLICATION_JSON).content("{\"value\":\"stolen@example.test\"}")).andExpect(MockMvcResultMatchers.status().isNotFound());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.delete((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{otherHeader})).andExpect(MockMvcResultMatchers.status().isNotFound());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.delete((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{ownerHeader})).andExpect(MockMvcResultMatchers.status().isOk());
        this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.get((String)"/api/vault/data/{id}", (Object[])new Object[]{dataId}).header("Authorization", new Object[]{ownerHeader})).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private RegisteredUser registerUser() throws Exception {
        String email = "vault-" + String.valueOf(UUID.randomUUID()) + "@example.test";
        String content = "{\"fullName\":\"Vault User\",\"email\":\"%s\",\"password\":\"StrongPass!7\"}\n".formatted(email);
        String response = this.mockMvc.perform((RequestBuilder)MockMvcRequestBuilders.post((String)"/api/auth/register", (Object[])new Object[0]).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath((String)"$.success", (Matcher)Matchers.is((Object)true))).andReturn().getResponse().getContentAsString();
        JsonNode data = this.objectMapper.readTree(response).path("data");
        return new RegisteredUser(data.path("userId").asLong(), email, data.path("accessToken").asText(), data.path("refreshToken").asText());
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private record RegisteredUser(long userId, String email, String accessToken, String refreshToken) {
    }

}
