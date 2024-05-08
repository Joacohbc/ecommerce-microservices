package com.microecommerce.ordersservice.models;

import com.microecommerce.utilitymodule.models.TimeStamped;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderMongoEventListener extends AbstractMongoEventListener<Order> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Order> event) {
        super.onBeforeConvert(event);

        // If it's an existing document on update
        if (event.getSource().getId() != null) {
            event.getSource().getTimeStamp().setUpdatedAt(LocalDateTime.now());
        } else {
            TimeStamped ts = new TimeStamped();
            ts.setCreatedAt(LocalDateTime.now());
            event.getSource().setTimeStamp(ts);
        }
    }
}
