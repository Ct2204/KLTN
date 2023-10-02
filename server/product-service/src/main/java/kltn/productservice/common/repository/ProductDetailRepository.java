package kltn.productservice.common.repository;



import kltn.productservice.common.entity.ProductDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, Long> {

    @Query("SELECT pd FROM ProductDetailEntity pd JOIN pd.product p WHERE p.id = :productId")
    List<ProductDetailEntity> findProductDetailByProduct(@Param("productId") Long productId);

    @Query( value ="SELECT * from product_detail where product_id = :id order by id",
            nativeQuery = true)
    List<ProductDetailEntity> getAllByProductIdInAscOrder (@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value ="DELETE FROM product_detail WHERE product_id = :id",nativeQuery = true)
    void deleteAllByProductId(@Param("id") Long id);
}
