package com.pih.api.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="shipment")
public class Shipment {

	@Id
	private String shipmentId;
	private String orderId;
	private String shipmentStatus;
}
