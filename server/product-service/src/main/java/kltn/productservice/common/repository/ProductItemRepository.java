package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItemEntity, Long> {
    @Query("SELECT pi FROM ProductItemEntity pi " +
            "JOIN pi.product p " +
            "WHERE p.id = :productId")
    List<ProductItemEntity> findAllItemsByProductId(@Param("productId") Long productId);

    @Query( value ="SELECT * from product_items where product_id = :id and status = true order by id",
            nativeQuery = true)
    List<ProductItemEntity> getAllByProductIdInAscOrder (@Param("id") Long id);

    @Query("SELECT p FROM ProductItemEntity p WHERE p.id IN (:ids) and p.product.id = :productId")
    List<ProductItemEntity> getAllByListIds(@Param("ids")List<Long> ids,@Param("productId") Long productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product_items pi " +
            "SET status = false " +
            "WHERE id NOT IN :ids " +
            "AND product_id = :productId " +
            "AND status = true", nativeQuery = true)
    void updateProductItemByStatus(@Param("ids") List<Long> ids, @Param("productId") Long productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product_items pi " +
            "SET status = false " +
            "WHERE product_id = :productId " +
            "AND status = true", nativeQuery = true)
    void updateAllProductItemByStatus(@Param("productId") Long productId);



    //linh
    @Query("SELECT sum(p.quantity) FROM ProductItemEntity p WHERE  p.product.id = :productId")
    Long getQuantityProduct(@Param("productId") Long productId);


}
