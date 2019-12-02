package com.sahajsoft.hotel.automation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Light implements Equipment {
    private final int consumption = Equipment.DEFAULT_LIGHT_CONSUMPTION;
    @Setter
    private EquipmentStatus equipmentStatus;

}
