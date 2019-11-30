package com.sahajsoft.hotel.automation.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
class Hotel {
    @Getter
    private int numberOfFloors;
    private List<Corridor> mainCorridors;
    private List<Corridor> subCorridors;
    private Map<Integer, Floor> floors = new HashMap<>();

    private Hotel(int numberOfFloors, List<Corridor> mainCorridors, List<Corridor> subCorridors, Map<Integer, Floor> floors) {
        this.numberOfFloors = numberOfFloors;
        this.mainCorridors = mainCorridors;
        this.subCorridors = subCorridors;
        for (int i = 0; i < numberOfFloors; i++) {
            addFloor(mainCorridors, subCorridors, i + 1);
        }
    }

    private void addFloor(List<Corridor> mainCorridors, List<Corridor> subCorridors, int floorId) {
        Floor floor = new Floor(floorId);
        floor.addMainCorridors(mainCorridors, CorridorType.MAIN);
        floor.addSubCorridors(subCorridors, CorridorType.SUB);
        this.floors.put(floorId, floor);
    }

    Map<Integer, Floor> getFloors() {
        return floors;
    }
}
