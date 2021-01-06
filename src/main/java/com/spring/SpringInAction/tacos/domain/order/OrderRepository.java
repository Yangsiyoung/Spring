package com.spring.SpringInAction.tacos.domain.order;

import com.spring.SpringInAction.tacos.domain.user.TacoUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByDeliveryZip(String deliveryZip);
    List<Order> findByTacoUserOrderByPlacedAtDesc(TacoUser tacoUser, Pageable pageable);
}
