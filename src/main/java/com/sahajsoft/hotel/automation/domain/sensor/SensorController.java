package com.sahajsoft.hotel.automation.domain.sensor;

import com.sahajsoft.hotel.automation.domain.Floor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Value
public class SensorController {
    private final Set<Floor> observers = new HashSet<>();
    private static final SensorController SENSOR_CONTROLLER = new SensorController();

    public static SensorController getSensorController() {
        return SENSOR_CONTROLLER;
    }

    public void addFloor(Floor floor) {
        observers.add(floor);
    }

    public void removeFloor(Floor floor) {
        observers.remove(floor);
    }

    public void movementDetected(Movement movement) {
        log.info("Movement detected for floor {} ", movement);
        notifyFloors(movement);
    }

    private void notifyFloors(Movement movement) {
        observers
                .forEach(floor -> {
                    log.info("Notify floor: {} for movement: {}", floor.getFloorId(), movement);
                    floor.movementDetected(movement);
                });
    }
}
