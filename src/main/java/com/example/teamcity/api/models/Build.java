package com.example.teamcity.api.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Build extends BaseModel{
    private String id;
    private Long taskId;
    private String buildTypeId;
    private String status;
    private String state;
    private BuildType buildType;
}
