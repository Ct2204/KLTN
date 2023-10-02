package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {
    @Query("SELECT po FROM ProductOptionEntity po " +
            "JOIN po.product p " +
            "WHERE p.id = :productId")
    List<ProductOptionEntity> findProductOptionByProductId(@Param("productId") Long productId);

    @Query( value ="SELECT * from product_options where product_id = :id order by id",
            nativeQuery = true)
    List<ProductOptionEntity> getAllByProductIdInAscOrder (@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value ="DELETE FROM product_options p WHERE product_id = :id",nativeQuery = true)
    void deleteAllByProductId(@Param("id") Long id);
}
