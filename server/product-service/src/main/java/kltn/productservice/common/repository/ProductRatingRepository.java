package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductEntity;
import kltn.productservice.common.entity.ProductRatingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRatingEntity,Long> {

    Page<ProductRatingEntity> findByProduct(ProductEntity product, Pageable pageable);

    @Query("select count(*) from ProductRatingEntity where product.id= :productId")
    long countAllRatingByProductId(@Param("productId") Long productId);

    @Query("SELECT SUM(starRating) from ProductRatingEntity where product.id= :productId")
    long sumStarOfProduct(@Param("productId") Long productId);

}
