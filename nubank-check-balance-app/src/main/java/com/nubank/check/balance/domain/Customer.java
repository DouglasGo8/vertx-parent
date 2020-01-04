package com.nubank.check.balance.domain;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author nubank
 */
@Data
@AllArgsConstructor
public class Customer {

    private String ssn;
    private String email;
    private String customerId;
    private String customerName;

    /**
     * @param customer Object
     * @return JsonObject
     */
    public static JsonObject toJson(Customer customer) {
        return new JsonObject()
                .put("id", customer.getCustomerId())
                .put("ssn", customer.getSsn())
                .put("customerName", customer.getCustomerName())
                .put("email", customer.getEmail());
    }

    /**
     * @return default Unknown Customer
     */
    public static Customer toJsonUnknownUser() {

        return new Customer("0", "unknown@mail", "-1", "Invalid Customer");
    }
}