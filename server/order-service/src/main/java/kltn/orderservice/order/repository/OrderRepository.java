package kltn.orderservice.order.repository;

import kltn.orderservice.common.entity.OrderEntity;
import kltn.orderservice.common.vo.OrderStatusE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT od FROM OrderEntity od WHERE od.user.id = :id")
    List<OrderEntity> getAllOrderByUserId(Long id);

    @Query("SELECT od FROM OrderEntity od WHERE od.status = :status")
    List<OrderEntity> getAllOrderByStatus(OrderStatusE status);
}
