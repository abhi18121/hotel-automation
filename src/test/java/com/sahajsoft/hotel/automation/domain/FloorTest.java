package com.sahajsoft.hotel.automation.domain;

import com.google.common.collect.ImmutableList;
import com.sahajsoft.hotel.automation.domain.sensor.Movement;
import org.junit.Test;

import java.util.Map;

import static com.sahajsoft.hotel.automation.domain.CorridorFixture.getMainCorridor;
import static com.sahajsoft.hotel.automation.domain.CorridorFixture.getSubCorridor;
import static com.sahajsoft.hotel.automation.domain.CorridorType.MAIN;
import static com.sahajsoft.hotel.automation.domain.CorridorType.SUB;
import static com.sahajsoft.hotel.automation.domain.Equipment.DEFAULT_AIR_CONDITIONER_CONSUMPTION;
import static com.sahajsoft.hotel.automation.domain.Equipment.DEFAULT_LIGHT_CONSUMPTION;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class FloorTest {
    private Floor floor = new Floor(1);

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAddEmptyCorridors() {
        //given
        //when
        floor.addMainCorridors(emptyList(), SUB);
        //then
        //exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldAddMainCorridorsTypeOnly() {
        //given
        Corridor mainCorridor = getMainCorridor();
        //when
        floor.addMainCorridors(ImmutableList.of(mainCorridor), SUB);
        //then
        //exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldAddSubCorridorsTypeOnly() {
        //given
        Corridor mainCorridor = getSubCorridor();
        //when
        floor.addSubCorridors(ImmutableList.of(mainCorridor), MAIN);
        //then
        //exception
    }

    @Test
    public void shouldAddSingleCorridor() {
        //given
        Corridor mainCorridor = getMainCorridor();
        //when
        floor.addMainCorridors(ImmutableList.of(mainCorridor), MAIN);
        //then
        assertEquals(1, floor.getNumberOfMainCorridors());
    }

    @Test
    public void shouldGetDefaultAcStatusToOnForMainCorridor() {
        //given
        Corridor mainCorridor = getMainCorridor();
        //when
        floor.addMainCorridors(ImmutableList.of(mainCorridor), MAIN);
        //then
        assertEquals(EquipmentStatus.ON, floor.getMainCorridors().get(1).getAirConditioner().getEquipmentStatus());
    }

    @Test
    public void shouldGetDefaultLightStatusToOnForMainCorridor() {
        //given
        Corridor mainCorridor = getMainCorridor();
        //when
        floor.addMainCorridors(ImmutableList.of(mainCorridor), MAIN);
        //then
        assertEquals(EquipmentStatus.ON, floor.getMainCorridors().get(1).getLight().getEquipmentStatus());
    }

    @Test
    public void shouldGetDefaultAcStatusToOnForSubCorridor() {
        //given
        Corridor mainCorridor = getSubCorridor();
        //when
        floor.addSubCorridors(ImmutableList.of(mainCorridor), SUB);
        //then
        assertEquals(EquipmentStatus.ON, floor.getSubCorridors().get(1).getAirConditioner().getEquipmentStatus());
    }

    @Test
    public void shouldGetDefaultLightConsumptionOfUnit() {
        //given
        Corridor mainCorridor = getMainCorridor();
        //when
        floor.addMainCorridors(ImmutableList.of(mainCorridor), MAIN);
        //then
        assertEquals(DEFAULT_LIGHT_CONSUMPTION, floor.getMainCorridors().get(1).getLight().getConsumption());
    }

    @Test
    public void shouldGetDefaultAcConsumptionOfUnit() {
        //given
        Corridor mainCorridor = getSubCorridor();
        //when
        floor.addSubCorridors(ImmutableList.of(mainCorridor), SUB);
        //then
        assertEquals(DEFAULT_AIR_CONDITIONER_CONSUMPTION, floor.getSubCorridors().get(1).getAirConditioner().getConsumption());
    }

    @Test
    public void shouldGetDefaultLightStatusToOnForSubCorridor() {
        //given
        Corridor mainCorridor = getSubCorridor();
        //when
        floor.addSubCorridors(ImmutableList.of(mainCorridor), SUB);
        //then
        assertEquals(EquipmentStatus.OFF, floor.getSubCorridors().get(1).getLight().getEquipmentStatus());
    }

    @Test
    public void shouldAddMainCorridors() {
        //given
        Corridor mainCorridor = getSubCorridor();
        //when
        floor.addMainCorridors(ImmutableList.of(mainCorridor), MAIN);
        //then
        Map<Integer, Corridor> mainCorridors = floor.getMainCorridors();
        assertEquals(1, mainCorridors.size());
        Map<Integer, Corridor> subCorridors = floor.getSubCorridors();
        assertEquals(0, subCorridors.size());
    }

    @Test
    public void shouldAddSubCorridors() {
        //given
        Corridor mainCorridor = getMainCorridor();
        //when
        floor.addSubCorridors(ImmutableList.of(mainCorridor), SUB);
        //then
        Map<Integer, Corridor> mainCorridors = floor.getMainCorridors();
        assertEquals(0, mainCorridors.size());
        Map<Integer, Corridor> subCorridors = floor.getSubCorridors();
        assertEquals(1, subCorridors.size());
    }

    @Test
    public void shouldChangeStatusOfLightToOnWhenMovementDetects() {
        //given
        Corridor subCorridor = getSubCorridor();
        floor.addSubCorridors(ImmutableList.of(subCorridor), SUB);
        Movement movement = getMovement(subCorridor);
        //when
        floor.movementDetected(movement);
        //then
        Map<Integer, Corridor> subCorridors = floor.getSubCorridors();
        assertEquals(EquipmentStatus.ON, subCorridors.get(subCorridor.getCorridorId()).getLight().getEquipmentStatus());
    }

    private Movement getMovement(Corridor subCorridor) {
        return Movement.builder()
                .floorId(floor.getFloorId())
                .isMovementDetected(true)
                .subCorridorId(subCorridor.getCorridorId())
                .build();
    }

}