package com.sahajsoft.hotel.automation.domain;

import com.sahajsoft.hotel.automation.domain.sensor.Movement;

public class MovementFixture {
    public static Movement buildMovementForCorridor(Corridor subCorridor, Floor floor) {
        return Movement.builder()
                .floorId(floor.getFloorId())
                .isMovementDetected(true)
                .subCorridorId(subCorridor.getCorridorId())
                .build();
    }
}
