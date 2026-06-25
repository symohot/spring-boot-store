package com.codewithmosh.store.orders;

public class OrderNotBelongsToThisCustomer extends RuntimeException {
    public OrderNotBelongsToThisCustomer() {
        super("Order not belongs to this customer");
    }
}
