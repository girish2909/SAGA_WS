package com.pih.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class CancelPaymentCommand {
 
	@TargetAggregateIdentifier
	private String paymentId;
	private String orderId;
	private String paymentStatus = "CANCELLED";
}
