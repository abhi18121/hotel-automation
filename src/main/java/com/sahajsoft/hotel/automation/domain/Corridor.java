package com.sahajsoft.hotel.automation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
class Corridor {
    private Light light;
    private AirConditioner airConditioner;
    @NonNull
    private CorridorType corridorType;
    @Setter
    private int corridorId;
}
