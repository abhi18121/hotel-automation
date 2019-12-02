package com.sahajsoft.hotel.automation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.sahajsoft.hotel.automation.domain.Equipment.DEFAULT_AIR_CONDITIONER_CONSUMPTION;

@Builder
@Getter
@ToString
public class AirConditioner {
    public final int consumption = DEFAULT_AIR_CONDITIONER_CONSUMPTION;
    @Setter
    private EquipmentStatus equipmentStatus;
}
