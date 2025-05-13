package com.mongodb.api.hrms.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressDto {
    @NotNull(message = "street must not be null")
    private String street;

    @NotNull(message = "city must not be null")
    private String city;

    @NotNull(message = "state must not be null")
    private String state;

    private String zipCode;
}
