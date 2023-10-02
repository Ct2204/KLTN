package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductOptionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductOptionDetailRepository extends JpaRepository<ProductOptionDetailEntity,Long> {
    @Query("SELECT pod FROM ProductOptionDetailEntity pod " +
            "JOIN pod.product p " +
            "WHERE p.id = :productId AND pod.id = :productOptionDetailId")
    ProductOptionDetailEntity findProductOptionDetailByProductId(@Param("productId") Long productId,
                                                                       @Param("productOptionDetailId") Long productOptionDetailId);

    @Query( value ="SELECT * from product_option_detail where product_option_id = :id order by id",
            nativeQuery = true)
    List<ProductOptionDetailEntity> getAllByProductIdInAscOrder (@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM product_option_detail p WHERE product_id = :id",nativeQuery = true)
    void deleteAllByProductId(@Param("id") Long id);
}
