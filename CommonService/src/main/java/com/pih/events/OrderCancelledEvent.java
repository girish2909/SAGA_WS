package com.pih.events;

import lombok.Data;

@Data
public class OrderCancelledEvent {

	private String orderId;
	private String orderStatus;
}
