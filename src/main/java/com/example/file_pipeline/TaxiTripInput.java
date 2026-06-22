package com.example.file_pipeline;

import lombok.Data;

@Data
public class TaxiTripInput {
    private String vendorId;
    private String tpepPickupDatetime;
    private String tpepDropoffDatetime;
    private String passengerCount;
    private String tripDistance;
    private String fareAmount;
    private String tipAmount;
    private String totalAmount;
}