package com.pih.api.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pih.api.command.CreateOrderCommand;
import com.pih.api.model.OrderRestModel;

@RestController
@RequestMapping("/order")
public class OrderCommandController {
	
	private CommandGateway commandGateway;
	
	public OrderCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createOrder(@RequestBody OrderRestModel order) {
		
		String orderId =  UUID.randomUUID().toString();
		
		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
				.orderId(orderId)
				.addressId(order.getAddressId())
				.productId(order.getProductId())
				.quantity(order.getQuantity())
				.userId(order.getUserId())
				.orderStatus("CREATED")
				.build();
				
		commandGateway.sendAndWait(createOrderCommand);
		
		return "Order is Created";
	}
	

}
