package com.pih.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.pih.model.CardDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidatePaymentCommand{
	
	@TargetAggregateIdentifier
	private String paymentId;
	private String orderId;
	private CardDetails cardDetails;

}
