package kltn.orderservice.common.repository;


import kltn.orderservice.common.entity.ProductItemDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductItemDetailRepository extends JpaRepository<ProductItemDetailEntity,Long> {

    @Query( value ="SELECT * from product_item_detail where product_item_id = :id order by id",
            nativeQuery = true)
    List<ProductItemDetailEntity> getAllByProductItemId (@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductItemDetailEntity pid " +
            "WHERE pid.productItem IN (SELECT pi FROM ProductItemEntity pi WHERE pi.product.id = :id)")
    void deleteAllByProductId(@Param("id") Long productId);

}
