package com.pih.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderShippedEvent {

	private String shipmentId;
	private String orderId;
	private String shipmentStatus;
	
}
