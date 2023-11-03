package com.redhat.training.conference;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;


@Entity
public class Schedule extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "venue_id", nullable = false)
    public int venueId;

    public LocalDate date;

    public LocalTime startTime;

    public Duration duration;

    public static long deleteById(int id) {
        return delete("id = ?1", id);
    }

    public static Schedule merge(int id, final Schedule newSchedule) {
        Optional<Schedule> schedule = Schedule.findByIdOptional(id);
        if (schedule.isPresent()) {
            schedule.map(s -> {
                s.venueId = newSchedule.venueId;
                s.date = newSchedule.date;
                s.startTime = newSchedule.startTime;
                s.duration = newSchedule.duration;
                return s;
            }).orElseThrow().persist();
        }
        return schedule.orElseThrow();
    }

    @Override
    public String toString() {
        return Schedule.class.getName() + "[id=" + this.id + "]";
    }
}
