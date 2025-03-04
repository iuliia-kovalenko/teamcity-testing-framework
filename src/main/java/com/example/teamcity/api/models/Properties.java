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
public class Properties extends BaseModel {
    private Integer count;
    private List<Property> property;

//    protected static Properties createPropertiesFromList(List<Property> propertiesList) {
//        return Properties.builder()
//                   .count(propertiesList.size())
//                   .property(propertiesList)
//                   .build();
//    }

    public static Properties createDefaultProperties() {
        return Properties.builder()
                   .count(3)
                   .property(List.of(
                       Property.builder().name("script.content").value("echo \"Hello World!\"").build(),
                       Property.builder().name("teamcity.step.mode").value("default").build(),
                       Property.builder().name("use.custom.script").value("true").build()
                   ))
                   .build();
    }
}