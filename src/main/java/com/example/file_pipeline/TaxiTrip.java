package com.example.file_pipeline;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "taxi_trips")
@Data
public class TaxiTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_id")
    private Integer vendorId;

    @Column(name = "pickup_datetime")
    private LocalDateTime pickupDatetime;

    @Column(name = "dropoff_datetime")
    private LocalDateTime dropoffDatetime;

    @Column(name = "passenger_count")
    private Integer passengerCount;

    @Column(name = "trip_distance")
    private Double tripDistance;

    @Column(name = "fare_amount")
    private Double fareAmount;

    @Column(name = "tip_amount")
    private Double tipAmount;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "tip_percentage")
    private Double tipPercentage;

    @Column(name = "trip_type")
    private String tripType;
}