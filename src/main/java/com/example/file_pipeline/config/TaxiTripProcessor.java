package com.example.file_pipeline.config;

import com.example.file_pipeline.TaxiTrip;
import com.example.file_pipeline.TaxiTripInput;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TaxiTripProcessor implements ItemProcessor<TaxiTripInput, TaxiTrip> {

    private final TaxiDateTimeConverter dateTimeConverter = new TaxiDateTimeConverter();


    @Override
    public @Nullable TaxiTrip process(TaxiTripInput input) throws Exception {

        double distance = parseDouble(input.getTripDistance());
        double fare = parseDouble(input.getFareAmount());
        double tip = parseDouble(input.getTipAmount());
        double total = parseDouble(input.getTotalAmount());

        if (distance <= 0 || total <= 0 || fare < 0) {
            return null; // Spring Batch null dönen kayıtları otomatik olarak pas geçer (yazmaz)
        }

        TaxiTrip trip = new TaxiTrip();
        trip.setVendorId(parseInt(input.getVendorId()));
        trip.setPassengerCount(parseInt(input.getPassengerCount()));
        trip.setTripDistance(distance);
        trip.setFareAmount(fare);
        trip.setTipAmount(tip);
        trip.setTotalAmount(total);

        trip.setPickupDatetime(dateTimeConverter.convert(input.getTpepPickupDatetime()));
        trip.setDropoffDatetime(dateTimeConverter.convert(input.getTpepDropoffDatetime()));

        // Tip perc calculation
        if (fare > 0) {
            trip.setTipPercentage((tip / fare) * 100);
        } else {
            trip.setTipPercentage(0.0);
        }

        // Distance classifier
        if (distance < 2.0) {
            trip.setTripType("SHORT");
        } else if (distance <= 7.0) {
            trip.setTripType("MEDIUM");
        } else {
            trip.setTripType("LONG");
        }

        return trip;
    }

    private double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value.trim()) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseInt(String value) {
        try {
            return value != null ? Integer.parseInt(value.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}