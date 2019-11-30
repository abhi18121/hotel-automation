package com.sahajsoft.hotel.automation.domain.sensor;

public interface Observer {
    void movementDetected(Movement movement);
}
