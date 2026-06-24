package com.codewithmosh.store.exceptions;

public class OrderNotBelongsToThisCustomer extends RuntimeException {
    public OrderNotBelongsToThisCustomer() {
        super("Order not belongs to this customer");
    }
}
