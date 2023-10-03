package kltn.orderservice.order.repository;


import kltn.orderservice.common.entity.OrderDetailEntity;
import kltn.orderservice.order.dto.OrderDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {

    @Query("SELECT new kltn.orderservice.order.dto.OrderDetailDto(odd.id,odd.order.id,odd.productItem.id,odd.quantity,odd.status)FROM OrderDetailEntity odd  WHERE odd.order.id = :id")
    List<OrderDetailDto> getOrderDetailByOrderId(Long id);
}
