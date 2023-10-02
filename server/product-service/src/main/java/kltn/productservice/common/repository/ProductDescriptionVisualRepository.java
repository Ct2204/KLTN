package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductDescriptionVisualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDescriptionVisualRepository extends JpaRepository<ProductDescriptionVisualEntity, Long> {

    @Query( value ="SELECT * from product_description_visual where product_id = :id order by id",
            nativeQuery = true)
    List<ProductDescriptionVisualEntity> getAllByProductIdInAscOrder (@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value ="DELETE FROM product_description_visual WHERE product_id = :id",nativeQuery = true)
    void deleteAllByProductId(@Param("id") Long id);
}
