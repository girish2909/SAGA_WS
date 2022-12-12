package com.pih.api.events;

import org.axonframework.common.BuilderUtils;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.pih.api.data.Shipment;
import com.pih.api.data.ShipmentRepository;
import com.pih.events.OrderShippedEvent;

@Component
public class ShipmentsEventHandler {

	private ShipmentRepository shipmentRepository;
	
	
	
	public ShipmentsEventHandler(ShipmentRepository shipmentRepository) {
		super();
		this.shipmentRepository = shipmentRepository;
	}



	@EventHandler
	public void on(OrderShippedEvent event) {
		Shipment shipment=new Shipment();
		BeanUtils.copyProperties(event, shipment);
		
		shipmentRepository.save(shipment);
	}
}
