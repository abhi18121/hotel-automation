package com.sahajsoft.hotel.automation.domain;

import java.util.List;

public interface CorridorStrategy {
    List<Corridor> apply(List<Corridor> corridors);

    static CorridorStrategy mainCorridor() {
        return mainCorridors -> {
            mainCorridors.forEach(corridor -> {
                corridor.getLight().setEquipmentStatus(EquipmentStatus.ON);
                corridor.getAirConditioner().setEquipmentStatus(EquipmentStatus.ON);
            });
            return mainCorridors;
        };
    }

    static CorridorStrategy subCorridor() {
        return subCorridors -> {
            subCorridors.forEach(corridor -> {
                corridor.getLight().setEquipmentStatus(EquipmentStatus.OFF);
                corridor.getAirConditioner().setEquipmentStatus(EquipmentStatus.ON);
            });
            return subCorridors;
        };
    }
}
