package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductVisualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductVisualRepository extends JpaRepository<ProductVisualEntity, Long> {

    @Query( value ="SELECT * from product_visual where product_option_detail_id = :id order by id",
            nativeQuery = true)
    List<ProductVisualEntity> getAllByProductOptionDetailIdInAscOrder (@Param("id") Long id);


    @Transactional
    @Modifying
    @Query("DELETE FROM ProductVisualEntity p WHERE p.product.id = :id and p.productOptionDetail IS NOT NULL")
    void deleteAllByProductId(@Param("id") Long id);
}
