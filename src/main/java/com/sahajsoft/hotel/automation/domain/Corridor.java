package com.sahajsoft.hotel.automation.domain;

import lombok.*;

@Builder
@Getter
@ToString
public class Corridor {
    private Light light;
    private AirConditioner airConditioner;
    @NonNull
    private CorridorType corridorType;
    @Setter
    private int corridorId;
}
