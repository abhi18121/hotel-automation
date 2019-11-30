package com.sahajsoft.hotel.automation.domain;

public class CorridorFixture {

    public static Corridor getMainCorridor() {
        AirConditioner airConditioner = AirConditionerFixture.getAirConditioner();
        Light light = LightFixture.getLightDefaultOn();
        return Corridor
                .builder()
                .corridorType(CorridorType.MAIN)
                .airConditioner(airConditioner)
                .light(light)
                .build();
    }

    public static Corridor getSubCorridor() {
        AirConditioner airConditioner = AirConditionerFixture.getAirConditioner();
        Light light = LightFixture.getLightDefaultOff();
        return Corridor
                .builder()
                .corridorType(CorridorType.SUB)
                .airConditioner(airConditioner)
                .light(light)
                .build();
    }
}
