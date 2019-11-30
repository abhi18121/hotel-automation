package com.sahajsoft.hotel.automation.domain.sensor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Movement {
    private int floorId;
    private int subCorridorId;
    private boolean isMovementDetected;
}
