package com.pih.api.saga;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.pih.api.events.OrderCreatedEvent;
import com.pih.commands.CancelOrderCommand;
import com.pih.commands.CancelPaymentCommand;
import com.pih.commands.CompleteOrderCommand;
import com.pih.commands.ShipOrderCommand;
import com.pih.commands.ValidatePaymentCommand;
import com.pih.events.OrderCancelledEvent;
import com.pih.events.OrderCompletedEvent;
import com.pih.events.OrderShippedEvent;
import com.pih.events.PaymentCancelledEvent;
import com.pih.events.PaymentProcessedEvent;
import com.pih.model.User;
import com.pih.queries.GetUserPaymentDetailsQuery;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderProcessingSaga {

	@Autowired
	private transient CommandGateway commandGateway;

	@Autowired
	private transient QueryGateway queryGateway;

	public OrderProcessingSaga() {

	}

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	private void handle(OrderCreatedEvent event) {

		log.info("OrderCreatedEvent in saga for order id {}", event.getOrderId());

		GetUserPaymentDetailsQuery getUserPaymentDetailsQuery= new GetUserPaymentDetailsQuery(event.getUserId());
		User user= null;
		try {
			user= queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
		}catch(Exception ex) {
			log.error(ex.getMessage());
			// When any issue came to get the user details or in Payment validation then cancel the order
			cancelOrderCommand(event.getOrderId());
		}
		ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
				.cardDetails(user.getCardDetails())
				.orderId(event.getOrderId())
				.paymentId(UUID.randomUUID().toString())
				.build();
		commandGateway.sendAndWait(validatePaymentCommand);

	}

	private void cancelOrderCommand(String orderId) {
		// TODO Auto-generated method stub
		CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
		commandGateway.send(cancelOrderCommand);
	}

	@SagaEventHandler(associationProperty = "orderId")
	private void handle(PaymentProcessedEvent event) {
		log.info("PaymentProcessedEvent in Saga order Id :{} and PayementId :{}",event.getOrderId(),event.getPaymentId());
		try {
			ShipOrderCommand shipOrderCommand= ShipOrderCommand.builder()
					.shipmentId(UUID.randomUUID().toString())
					.orderId(event.getOrderId())
					.build();

			commandGateway.send(shipOrderCommand);
		}catch(Exception e){
			log.error(e.getMessage());
			cancelPaymentCommand(event);
		}
	}


	private void cancelPaymentCommand(PaymentProcessedEvent event) {
		// TODO Auto-generated method stub
		CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand(event.getPaymentId(), event.getOrderId());
		commandGateway.send(cancelPaymentCommand);

	}

	@SagaEventHandler(associationProperty = "orderId")
	private void handle(OrderShippedEvent event) {
		log.info("OrderShippedEvent in Saga order Id :{} and ShipmentId :{}",event.getOrderId(),event.getShipmentId());
		try {
			CompleteOrderCommand completeOrderCommand= CompleteOrderCommand.builder()
					.orderId(event.getOrderId())
					.orderStatus("APPROVED")
					.build();

			commandGateway.send(completeOrderCommand);
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}

	@SagaEventHandler(associationProperty = "orderId")
	@EndSaga
	private void handle(OrderCompletedEvent event) {
		log.info("OrderCompletedEvent in Saga order Id :{}",event.getOrderId());
	}

	// Order Cancelled event scenarios :::
	@SagaEventHandler(associationProperty = "orderId")
	@EndSaga
	private void handle(OrderCancelledEvent event) {
		log.info("OrderCancelledEvent in Saga order Id :{}",event.getOrderId());
	}


	// Payment Cancelled event scenarios :::
	@SagaEventHandler(associationProperty = "orderId")
	private void handle(PaymentCancelledEvent event) {
		log.info("PaymentCancelledEvent in Saga order Id :{}",event.getOrderId());
		cancelOrderCommand(event.getOrderId());
	}



}
