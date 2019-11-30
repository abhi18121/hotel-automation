package com.sahajsoft.hotel.automation.domain;

public class LightFixture {

    public static Light getLightDefaultOn() {
        return Light.builder()
                .equipmentStatus(EquipmentStatus.ON)
                .build();
    }

    public static Light getLightDefaultOff() {
        return Light.builder()
                .equipmentStatus(EquipmentStatus.OFF)
                .build();
    }
}
