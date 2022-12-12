package com.pih.controller;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pih.model.User;
import com.pih.queries.GetUserPaymentDetailsQuery;

@RestController
@RequestMapping("/users")
public class UserController {

	private transient QueryGateway queryGateway;
	
	public UserController(QueryGateway queryGateway) {
		super();
		this.queryGateway = queryGateway;
	}


	@GetMapping("{userId}")
	public User getUserPaymentDetails(@PathVariable String userId) {
		GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(userId);
		
		User user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
		
		return user;
	}
}
