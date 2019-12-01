package com.sahajsoft.hotel.automation.domain;

import com.google.common.collect.ImmutableMap;
import com.sahajsoft.hotel.automation.domain.sensor.Movement;
import com.sahajsoft.hotel.automation.domain.sensor.Observer;
import com.sahajsoft.hotel.automation.domain.sensor.SensorController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"sensorController"})
public class Floor implements Observer {
    private Map<CorridorType, Map<Integer, Corridor>> corridors = new HashMap<>();

    @Getter
    @EqualsAndHashCode.Include
    private int floorId;
    @Getter
    private int numberOfMainCorridors = 0;
    private int numberOfSubCorridors = 0;
    private SensorController sensorController = SensorController.getSensorController();
    private Map<Integer, ZonedDateTime> movementsCorridors = new HashMap<>();

    public Floor(int floorId) {
        this.floorId = floorId;
        log.info("Floor added with id: {} ", this.floorId);
        sensorController.addFloor(this);
        new Thread(createFloorThread()).start();
        log.info("Thread has started for floor id: {} ", this.floorId);
    }

    private Runnable createFloorThread() {
        Runnable runnable = () -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    if (movementsCorridors.size() >= 1) {
                        getSubCorridors()
                                .keySet()
                                .forEach(id -> {
                                    changeToDefaultStatus(id);
                                });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        return runnable;
    }

    private void changeToDefaultStatus(Integer id) {
        if (isBeforeOneMinuteOfLastMovement(id)) {
            Corridor corridor = getSubCorridors().get(id);
            log.info("Turning off AC and light of sub corridors {} at {}", corridor.getCorridorId(), now());
            corridor.getLight().setEquipmentStatus(OFF);
            corridor.getAirConditioner().setEquipmentStatus(OFF);
        }
    }

    private boolean isBeforeOneMinuteOfLastMovement(Integer id) {
        return movementsCorridors.get(id).plusMinutes(2).isBefore(now());
    }

    void addMainCorridors(List<Corridor> corridors, CorridorType corridorType) {
        validate(corridors, corridorType, MAIN);
        Map<Integer, Corridor> corridorsToBeAdded = enrichMainCorridors(corridors);
        this.corridors.put(corridorType, corridorsToBeAdded);
        log.info("Main corridors added {}", corridors.size());
    }

    private Map<Integer, Corridor> enrichSubCorridors(List<Corridor> corridors) {
        CorridorStrategy.subCorridor().apply(corridors);
        Map<Integer, Corridor> corridorsToBeAdded = new HashMap<>();
        for (Corridor corridor : corridors) {
            this.numberOfSubCorridors = this.numberOfSubCorridors + 1;
            corridor.setCorridorId(this.numberOfSubCorridors);
            corridorsToBeAdded.put(this.numberOfSubCorridors, corridor);
        }
        return corridorsToBeAdded;
    }

    void addSubCorridors(List<Corridor> corridors, CorridorType corridorType) {
        validate(corridors, corridorType, SUB);
        Map<Integer, Corridor> corridorsToBeAdded = enrichSubCorridors(corridors);
        this.corridors.put(corridorType, corridorsToBeAdded);
        log.info("Sub corridors added {} ", corridors.size());

    }

    private Map<Integer, Corridor> enrichMainCorridors(List<Corridor> corridors) {
        CorridorStrategy.mainCorridor().apply(corridors);
        Map<Integer, Corridor> corridorsToBeAdded = new HashMap<>();
        for (Corridor corridor : corridors) {
            this.numberOfMainCorridors = this.numberOfMainCorridors + 1;
            corridor.setCorridorId(this.numberOfMainCorridors);
            corridorsToBeAdded.put(this.numberOfMainCorridors, corridor);
        }
        return corridorsToBeAdded;
    }

    private void validate(List<Corridor> corridors, CorridorType actualCorridorType, CorridorType expectedCorridorType) {
        checkArgument(expectedCorridorType == actualCorridorType, "corridors should be " + expectedCorridorType);
        checkArgument(isNotEmpty(corridors), "corridors could not be empty");
    }

    private Map<CorridorType, Map<Integer, Corridor>> getCorridors() {
        return this.corridors;
    }

    public Map<Integer, Corridor> getMainCorridors() {
        return this.getCorridors().getOrDefault(MAIN, ImmutableMap.of());
    }

    public Map<Integer, Corridor> getSubCorridors() {
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
