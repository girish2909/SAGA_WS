package com.pih.api.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

	@Id
	private String paymentId;
	private String orderId;
	private Date timeStamp;
	private String paymentStatus;
	
}
