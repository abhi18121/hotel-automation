package com.sahajsoft.hotel.automation.domain;

public class AirConditionerFixture {

    public static AirConditioner getAirConditioner() {
        return AirConditioner.builder()
                .equipmentStatus(EquipmentStatus.ON)
                .build();
    }

}
