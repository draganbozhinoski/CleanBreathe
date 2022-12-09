package com.example.cleanbreathejava.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;

@Entity
@Table(name = "pm_particles")
@Data
public class Pm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String type;
    Double value;
    String localDateTime;
    protected Pm() {

    }
    public Pm(String type, Double value, String localDateTime) {
        this.type = type;
        this.value = value;
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Pm{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", localDateTime='" + localDateTime + '\'' +
                '}';
    }
}
