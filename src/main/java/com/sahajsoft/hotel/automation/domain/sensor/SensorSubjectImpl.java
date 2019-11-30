package com.sahajsoft.hotel.automation.domain.sensor;

import com.sahajsoft.hotel.automation.domain.Floor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SensorSubjectImpl implements SensorSubject {
    private Set<Floor> observers = new HashSet<>();
    private static final SensorSubject sensorSubject = new SensorSubjectImpl();

    public static SensorSubject getSensorSubject() {
        return sensorSubject;
    }

    @Override
    public void addObserver(Floor floor) {
        observers.add(floor);
    }

    @Override
    public void removeObserver(Floor floor) {
        observers.remove(floor);
    }

    @Override
    public void movementDetected(Movement movement) {
        log.info("Movement detected for floor {} ", movement);
        notifyObservers(movement);
    }

    private void notifyObservers(Movement movement) {
        observers
                .forEach(floor -> {
                    log.info("Notify floor: {} for movement: {}", floor.getFloorId(), movement);
                    floor.movementDetected(movement);
                });
    }
}
