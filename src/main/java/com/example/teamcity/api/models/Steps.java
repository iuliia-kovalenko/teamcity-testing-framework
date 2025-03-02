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
public class Steps extends BaseModel{
   @Builder.Default
   private Integer count = 1;
   private List<Step> step;

   public static Steps createDefaultSteps() {
      Step step = Step.builder()
                      .name("Step")
                      .properties(Properties.createDefaultProperties())
                      .build();

      return Steps.builder()
                 .count(1)
                 .step(List.of(step))
                 .build();
   }
}
