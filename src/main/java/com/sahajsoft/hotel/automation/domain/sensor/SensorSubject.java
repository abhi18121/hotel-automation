package com.sahajsoft.hotel.automation.domain.sensor;

import com.sahajsoft.hotel.automation.domain.Floor;

public interface SensorSubject {
    void addObserver(Floor flor);

    void removeObserver(Floor flor);

    void movementDetected(Movement movement);
}
