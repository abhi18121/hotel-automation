package com.sahajsoft.hotel.automation.domain;

import com.google.common.collect.ImmutableMap;
import com.sahajsoft.hotel.automation.domain.sensor.Movement;
import com.sahajsoft.hotel.automation.domain.sensor.Observer;
import com.sahajsoft.hotel.automation.domain.sensor.SensorSubject;
import com.sahajsoft.hotel.automation.domain.sensor.SensorSubjectImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.sahajsoft.hotel.automation.domain.CorridorType.MAIN;
import static com.sahajsoft.hotel.automation.domain.CorridorType.SUB;
import static com.sahajsoft.hotel.automation.domain.EquipmentStatus.OFF;
import static com.sahajsoft.hotel.automation.domain.EquipmentStatus.ON;
import static java.time.ZonedDateTime.now;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
public class Floor implements Observer {
    private Map<CorridorType, Map<Integer, Corridor>> corridors = new HashMap<>();

    @Getter
    private int floorId;
    @Getter
    private int numberOfCorridors = 0;
    private SensorSubject sensorSubject = SensorSubjectImpl.getSensorSubject();
    private Map<Integer, ZonedDateTime> movementsCorridors = new HashMap<>();

    Floor(int floorId) {
        this.floorId = floorId;
        log.info("Floor added with id: {} ", this.floorId);
        sensorSubject.addObserver(this);
        new Thread(createFloorThread()).start();
        log.info("Thread has started for floor id: {} ", this.floorId);
    }

    private Runnable createFloorThread() {
        Runnable runnable = () -> {
            while (true) {
                try {
                    Thread.sleep(20000);
                    if (movementsCorridors.size() >= 1) {
                        getSubCorridors()
                                .keySet()
                                .forEach(id -> {
                                    ZonedDateTime zonedDateTime = movementsCorridors.get(id);
                                    if (zonedDateTime.plusMinutes(2).isBefore(now())) {
                                        Corridor corridor = getSubCorridors().get(id);
                                        log.info("Turning of AC and light of sub corridors {} at {}", corridor.getCorridorId(), now());
                                        corridor.getLight().setEquipmentStatus(OFF);
                                        corridor.getAirConditioner().setEquipmentStatus(OFF);
                                    }
                                });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        return runnable;
    }

    private void addCorridors(List<Corridor> corridors, CorridorType corridorType) {
        checkArgument(isNotEmpty(corridors), "corridors could not be empty");
        Map<Integer, Corridor> corridorsToBeAdded = new HashMap<>();
        corridors.stream()
                .forEach(corridor -> {
                    this.numberOfCorridors = this.numberOfCorridors + 1;
                    corridor.setCorridorId(this.numberOfCorridors);
                    corridorsToBeAdded.put(this.numberOfCorridors, corridor);
                });
        this.corridors.put(corridorType, corridorsToBeAdded);
    }

    void addMainCorridors(List<Corridor> corridors, CorridorType corridorType) {
        checkArgument(MAIN == corridorType, "corridors should be MAIN");
        CorridorStrategy.mainCorridor().apply(corridors);
        addCorridors(corridors, MAIN);
        log.info("Main corridors added {}", corridors.size());
    }

    void addSubCorridors(List<Corridor> corridors, CorridorType corridorType) {
        checkArgument(SUB == corridorType, "corridors should be SUB");
        CorridorStrategy.subCorridor().apply(corridors);
        addCorridors(corridors, SUB);
        log.info("Sub corridors added {} ", corridors.size());

    }

    private Map<CorridorType, Map<Integer, Corridor>> getCorridors() {
        return this.corridors;
    }

    Map<Integer, Corridor> getMainCorridors() {
        return this.getCorridors().getOrDefault(MAIN, ImmutableMap.of());
    }

    Map<Integer, Corridor> getSubCorridors() {
        return this.getCorridors().getOrDefault(SUB, ImmutableMap.of());
    }

    @Override
    public void movementDetected(Movement movement) {
        log.info("Movement detected for floor: {} and movement is: {} ", this.getFloorId(), movement);
        if (isMovementDetectedOnFloor(movement)) {
            this.getSubCorridors()
                    .entrySet()
                    .stream()
                    .filter(integerCorridorEntry -> integerCorridorEntry.getKey() == movement.getSubCorridorId())
                    .forEach(entry -> {
                        movementsCorridors.put(movement.getSubCorridorId(), now());
                        this.getSubCorridors().get(movement.getSubCorridorId()).getLight().setEquipmentStatus(ON);
                        log.info("Changing status of light for SubCorridor {} to on, status changed {} ", movement.getSubCorridorId(), this.getSubCorridors().get(movement.getSubCorridorId()).getLight().getEquipmentStatus());
                    });
        }
    }

    private boolean isMovementDetectedOnFloor(Movement movement) {
        return floorId == movement.getFloorId() && movement.isMovementDetected();
    }

}
