package com.pih.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.pih.commands.CancelPaymentCommand;
import com.pih.commands.ValidatePaymentCommand;
import com.pih.events.PaymentCancelledEvent;
import com.pih.events.PaymentProcessedEvent;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class PaymentAggregate {

	@AggregateIdentifier
	private String paymentId;
	private String orderId;
	private String paymentStatus;
	
	public PaymentAggregate() {
		
	}
	
	@CommandHandler
	public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
		//validate the payment details
		//publish the payment process event
		log.info("ValidatePaymentCommand for Order id : {} and Payament id: {}",
				validatePaymentCommand.getOrderId(),validatePaymentCommand.getPaymentId());
		
		PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(validatePaymentCommand.getPaymentId(), validatePaymentCommand.getOrderId());
		
		AggregateLifecycle.apply(paymentProcessedEvent);
		log.info("PaymentProcessedEvent is applied");
		
	}
	
	@EventSourcingHandler
	public void on(PaymentProcessedEvent event) {
		this.orderId= event.getOrderId();
		this.paymentId = event.getPaymentId();
	}
	
	@CommandHandler
	public void handle(CancelPaymentCommand command) {
		
		PaymentCancelledEvent event = new PaymentCancelledEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}
	
	@CommandHandler
	public void on(PaymentCancelledEvent event) {
		this.paymentStatus = event.getPaymentStatus();
	}
}
