package com.sahajsoft.hotel.automation.domain.sensor;

import com.google.common.collect.ImmutableList;
import com.sahajsoft.hotel.automation.domain.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static com.sahajsoft.hotel.automation.domain.CorridorFixture.getSubCorridor;
import static com.sahajsoft.hotel.automation.domain.EquipmentStatus.OFF;
import static com.sahajsoft.hotel.automation.domain.EquipmentStatus.ON;
import static com.sahajsoft.hotel.automation.domain.MovementFixture.buildMovementForCorridor;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SensorControllerTest {

    @Mock
    private Floor floorMock;

    @Before
    public void before(){
        SensorController  sensorController = SensorController.getSensorController();
        sensorController.getObservers().removeAll(sensorController.getObservers());
    }

    @Test
    public void shouldAddFloorInObservers() {
        //given
        SensorController  sensorController = SensorController.getSensorController();
        Floor floor = new Floor(1);
        //when
        sensorController.addFloor(floor);
        //then
        Assert.assertEquals(1, sensorController.getObservers().size());

    }

    @Test
    public void shouldRemoveFloorFromObservers() {
        //given
        SensorController  sensorController = SensorController.getSensorController();
        Floor floor = new Floor(1);
        sensorController.addFloor(floor);
        //when
        sensorController.removeFloor(floor);
        //then
        Assert.assertEquals(0, sensorController.getObservers().size());
    }

    @Test
    public void shouldNotifyFloorsWhenMovementIsDetected() {
        //given
        SensorController  sensorController = SensorController.getSensorController();
        Corridor subCorridor = getSubCorridor();
        sensorController.addFloor(floorMock);
        when(floorMock.getFloorId()).thenReturn(1);
        Movement movement = buildMovementForCorridor(subCorridor, floorMock);
        doNothing().when(floorMock).movementDetected(movement);
        //when
        sensorController.movementDetected(movement);
        //then
        Assert.assertEquals(1, sensorController.getObservers().size());
    }

    @Test
    public void shouldNotNotifyFloorsForEmptyFloors() {
        //given
        SensorController  sensorController = SensorController.getSensorController();
        Corridor subCorridor = getSubCorridor();
        when(floorMock.getFloorId()).thenReturn(1);
        Movement movement = buildMovementForCorridor(subCorridor, floorMock);
        //when
        sensorController.movementDetected(movement);
        //then
        Assert.assertEquals(0, sensorController.getObservers().size());
        verify(floorMock, times(0)).movementDetected(movement);
    }

    @Test
    public void defaultStateOfEquipmentsOnFloor() {
        //given
        Corridor mainCorridorOneOfFloorOne = CorridorFixture.getMainCorridor();
        Corridor subCorridorOneOfFloorOne = CorridorFixture.getSubCorridor();
        Corridor subCorridorTwoOfFloorOne = CorridorFixture.getSubCorridor();

        //when
        Hotel hotel = Hotel.builder()
                .numberOfFloors(1)
                .mainCorridors(ImmutableList.of(mainCorridorOneOfFloorOne))
                .subCorridors(ImmutableList.of(subCorridorOneOfFloorOne, subCorridorTwoOfFloorOne))
                .build();

        //then
        //assert floors status
        Map<Integer, Floor> floors = hotel.getFloors();
        assertEquals(1, floors.size());
        Floor floor = floors.get(1);
        Map<Integer, Corridor> mainCorridors = floor.getMainCorridors();
        assertEquals(1, mainCorridors.size());

        Map<Integer, Corridor> subCorridors = floor.getSubCorridors();
        Assert.assertEquals(2, subCorridors.size());

        //assert sub corridors status
        assertSubCorridorDefaultStatus(subCorridors.get(1));
        assertSubCorridorDefaultStatus(subCorridors.get(2));

        //assert main corridor status
        assertMainCorridorDefaultStatus(mainCorridors.get(1));

    }


    @Test
    public void movementInSubCorridorTwoOnFloor() {
        //given
        SensorController  sensorController = SensorController.getSensorController();
        Corridor mainCorridorOneOfFloorOne = CorridorFixture.getMainCorridor();
        Corridor subCorridorOneOfFloorOne = CorridorFixture.getSubCorridor();
        Corridor subCorridorTwoOfFloorOne = CorridorFixture.getSubCorridor();

        Hotel hotel = Hotel.builder()
                .numberOfFloors(1)
                .mainCorridors(ImmutableList.of(mainCorridorOneOfFloorOne))
                .subCorridors(ImmutableList.of(subCorridorOneOfFloorOne, subCorridorTwoOfFloorOne))
                .build();

        Movement movement = buildMovementForCorridor(subCorridorTwoOfFloorOne, hotel.getFloors().get(1));
        //when
        sensorController.movementDetected(movement);

        //then
        //assert floors status
        Map<Integer, Floor> floors = hotel.getFloors();
        assertEquals(1, floors.size());
        Floor floor = floors.get(1);
        Map<Integer, Corridor> mainCorridors = floor.getMainCorridors();
        assertEquals(1, mainCorridors.size());

        Map<Integer, Corridor> subCorridors = floor.getSubCorridors();
        Assert.assertEquals(2, subCorridors.size());

        //assert sub corridors status
        assertSubCorridorDefaultStatus(subCorridors.get(1));
        assertMovementOfSubCorridorStatus(subCorridors.get(2));

        //assert main corridor status
        assertMainCorridorDefaultStatus(mainCorridors.get(1));

        System.out.println(floor);

    }

    private void assertMovementOfSubCorridorStatus(Corridor corridor) {
        assertAcStatus(corridor, ON);
        assertLightStatus(corridor, ON);
    }

    private void assertSubCorridorDefaultStatus(Corridor corridor) {
        assertAcStatus(corridor, ON);
        assertLightStatus(corridor, OFF);
    }

    private void assertMainCorridorDefaultStatus(Corridor corridor) {
        assertAcStatus(corridor, ON);
        assertLightStatus(corridor, ON);
    }

    private void assertLightStatus(Corridor corridor, EquipmentStatus equipmentStatus) {
        Light light = corridor.getLight();
        assertEquals(equipmentStatus, light.getEquipmentStatus());
    }

    private void assertAcStatus(Corridor corridor, EquipmentStatus equipmentStatus) {
        AirConditioner airConditioner = corridor.getAirConditioner();
        assertEquals(equipmentStatus, airConditioner.getEquipmentStatus());
    }
}