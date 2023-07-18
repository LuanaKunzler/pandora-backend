package com.pandora.backend.model.request.order;

import com.pandora.backend.model.entity.OrderStatus;
import lombok.Data;

import javax.persistence.Column;

@Data
public class UpdateOrderRequest {

    private OrderStatus shipped;

    private String cargoFirm;

    private String trackingNumber;
}
