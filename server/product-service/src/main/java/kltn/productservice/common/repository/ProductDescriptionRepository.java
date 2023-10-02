package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductDescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductDescriptionRepository extends JpaRepository<ProductDescriptionEntity, Long> {
    @Query("SELECT pd FROM ProductDescriptionEntity pd JOIN pd.product p " +
            "WHERE p.id = :productId")
    List<ProductDescriptionEntity> findByProduct(@Param("productId") Long productId);

    @Transactional
    @Modifying
    @Query(value ="DELETE FROM product_description WHERE product_id = :id",nativeQuery = true)
    void deleteAllByProductId(@Param("id") Long id);
}
