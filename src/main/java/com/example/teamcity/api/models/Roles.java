package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Roles extends BaseModel {
    private List<Role> role;

    public static Roles generateRoles(List<Role> roles) {
        return Roles.builder()
                   .role(roles)
                   .build();
    }

    public static Roles generateRoles(Role... roles) {
        return generateRoles(List.of(roles));
    }
}