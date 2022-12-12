package com.pih.api.events;

import java.util.Date;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.pih.api.data.Payment;
import com.pih.api.data.PaymentRepository;
import com.pih.events.PaymentCancelledEvent;
import com.pih.events.PaymentProcessedEvent;

@Component
public class PaymentsEventHandler {

	private PaymentRepository paymentRepository;
	
	public PaymentsEventHandler(PaymentRepository paymentRepository) {
		super();
		this.paymentRepository = paymentRepository;
	}



	@EventHandler
	public void on(PaymentProcessedEvent event) {
		Payment payment=Payment.builder()
				.paymentId(event.getPaymentId())
				.orderId(event.getOrderId())
				.paymentStatus("COMPLETED")
				.timeStamp(new Date())
				.build();
		
		paymentRepository.save(payment);
	}
	
	
	@EventHandler
	public void on(PaymentCancelledEvent event) {
		Payment payment=paymentRepository.findById(event.getPaymentId()).get();
		payment.setPaymentStatus(event.getPaymentStatus());
		paymentRepository.save(payment);
	}
}
