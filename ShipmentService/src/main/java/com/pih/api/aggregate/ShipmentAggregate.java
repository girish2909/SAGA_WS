package com.pih.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.pih.commands.ShipOrderCommand;
import com.pih.events.OrderShippedEvent;

@Aggregate
public class ShipmentAggregate {

	@AggregateIdentifier
	private String shipmentId;
	private String orderId;
	private String shipmentStatus;
	
	public ShipmentAggregate() {
		
	}
	
	@CommandHandler
	public ShipmentAggregate(ShipOrderCommand shipOrderCommand) {
		// vAlidate the command
		// publish the Order shipped event
		OrderShippedEvent orderShippedEvent = OrderShippedEvent.builder()
				.shipmentId(shipOrderCommand.getShipmentId())
				.orderId(shipOrderCommand.getOrderId())
				.shipmentStatus("COMPLETED")
				.build();
		
		AggregateLifecycle.apply(orderShippedEvent);
		
	}
	
	@EventSourcingHandler
	public void on(OrderShippedEvent event) {
		this.orderId=event.getOrderId();
		this.shipmentId=event.getShipmentId();
		this.shipmentStatus=event.getShipmentStatus();
	}
}
