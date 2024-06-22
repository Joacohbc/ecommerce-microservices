package com.microecommerce.customerservice.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Phone {
    // TODO: Add country code as a Enum
    private Long number;
    private String countryCode;

    @Enumerated(EnumType.ORDINAL)
    private PhoneType type;

    public enum PhoneType { HOME, MOBILE }
}
