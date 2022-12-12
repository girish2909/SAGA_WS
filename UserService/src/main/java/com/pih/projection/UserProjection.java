package com.pih.projection;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.pih.model.CardDetails;
import com.pih.model.User;
import com.pih.queries.GetUserPaymentDetailsQuery;

@Component
public class UserProjection {
	
	@QueryHandler
	public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
		//Ideally get the card details from the Database
		CardDetails cardDetails = CardDetails
			.builder().name("Girish Mishra")
			.validUntilMonth(01)
			.validUntilYear(2023)
			.cardNumber("123456789")
			.cvv(123)
			.build();
		
		return User.builder().userId(query.getUserId())
				.firstName("Girish")
				.lastName("Mishra")
				.cardDetails(cardDetails)
				.build();
	}

}
