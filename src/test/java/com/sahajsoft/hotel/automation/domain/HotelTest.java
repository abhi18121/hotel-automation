package com.sahajsoft.hotel.automation.domain;

import com.google.common.collect.ImmutableList;
import com.sahajsoft.hotel.automation.domain.sensor.Movement;
import com.sahajsoft.hotel.automation.domain.sensor.SensorController;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HotelTest {

    @Test
    public void shouldAddFloors() throws InterruptedException {
        //given
        Corridor mainCorridor = CorridorFixture.getMainCorridor();
        Corridor subCorridor = CorridorFixture.getSubCorridor();
        //when
        Hotel hotel = Hotel.builder()
                .numberOfFloors(1)
                .mainCorridors(ImmutableList.of(mainCorridor))
                .subCorridors(ImmutableList.of(subCorridor))
                .build();
        SensorController sensorController = SensorController.getSensorController();
        Movement movement = Movement.builder()
                .floorId(hotel.getFloors().get(1).getFloorId())
                .isMovementDetected(true)
                .subCorridorId(subCorridor.getCorridorId())
                .build();
        sensorController.movementDetected(movement);
        Thread.currentThread().sleep(5000);
        //then
        Map<Integer, Floor> floors = hotel.getFloors();
        assertEquals(1, floors.size());
        assertEquals(1, hotel.getNumberOfFloors());

        Map<Integer, Corridor> mainCorridors = floors.get(1).getMainCorridors();
        assertEquals(1, mainCorridors.size());
        Map<Integer, Corridor> subCorridors = floors.get(1).getSubCorridors();
        assertEquals(1, subCorridors.size());

    }

}
