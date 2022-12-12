package com.pih.api.aggregate;

import java.nio.channels.CancelledKeyException;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.pih.api.command.CreateOrderCommand;
import com.pih.api.events.OrderCreatedEvent;
import com.pih.commands.CancelOrderCommand;
import com.pih.commands.CompleteOrderCommand;
import com.pih.events.OrderCancelledEvent;
import com.pih.events.OrderCompletedEvent;

@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;
	private String productId;
	private String userId;
	private String addressId;
	private Integer quantity;
	private String orderStatus;
	
	public OrderAggregate() {
	}
	
	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) {
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
		AggregateLifecycle.apply(orderCreatedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderCreatedEvent event) {
		this.orderId = event.getOrderId();
		this.addressId=event.getAddressId();
		this.productId=event.getProductId();
		this.userId = event.getUserId();
		this.quantity= event.getQuantity();
		this.orderStatus=event.getOrderStatus();
	}
	
	@CommandHandler
	public void handle(CompleteOrderCommand completeOrderCommand) {
		//Validate the command
		//publish the order completed event
		OrderCompletedEvent orderCompletedEvent= OrderCompletedEvent.builder()
				  .orderId(completeOrderCommand.getOrderId())
				  .orderStatus(completeOrderCommand.getOrderStatus())
				.build();
		AggregateLifecycle.apply(orderCompletedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderCompletedEvent event) {
		this.orderId = event.getOrderId();
		this.orderStatus = event.getOrderStatus();
	}
	
	
	@CommandHandler
	public void handle(CancelOrderCommand cancelOrderCommand) {
		OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent();
		BeanUtils.copyProperties(cancelOrderCommand, orderCancelledEvent);
		AggregateLifecycle.apply(orderCancelledEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderCancelledEvent orderCancelledEvent) {
		this.orderId = orderCancelledEvent.getOrderId();
		this.orderStatus = orderCancelledEvent.getOrderStatus();
	}
}
