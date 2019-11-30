package com.sahajsoft.hotel.automation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
class Light implements Equipment {
    private final int consumption = Equipment.DEFAULT_LIGHT_CONSUMPTION;
    @Setter
    private EquipmentStatus equipmentStatus;
}
