package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByCustomerId(Long userId);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o where o.customer=:customer")
    List<Order> getOrdersByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "items.product")
    @Query("select o from Order o where o.id=:orderId")
    Optional<Order> getOrderWithItems(@Param("orderId") Long orderId);
}
