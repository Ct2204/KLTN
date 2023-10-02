package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductEntity;
import kltn.productservice.product.dto.ProductSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity" +
            " p WHERE p.id = :productId")
    Optional<ProductEntity> findByProduct(@Param("productId") Long productId);

    @Query("SELECT p FROM ProductEntity p")
    Page<ProductEntity> findListProduct(Pageable pageable);

    Optional<ProductEntity> findOneById(Long id);

    @Query("select new kltn.productservice.product.dto.ProductSearchResDto(p.id, MIN(i.url),p.title,  avg(r.starRating) ,p.price,p.priceSales ) " +
            "From ProductEntity p " +
            " left join ProductVisualEntity i on p.id=i.product.id " +
            " left join ProductRatingEntity r on r.product.id = p.id " +
            "WHERE ( (p.title) ILIKE concat('%',:keyword, '%')) " +
            "or (unaccent(p.title) ILIKE concat('%',:keyword, '%')) "+
            "GROUP BY p.id")
    Page<ProductSearchResDto> getPageResultSearch(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "WITH RECURSIVE product_categories_tree(id, title, parent_id) AS (" +
            " SELECT id, title, parent_id" +
            " FROM product_categories" +
            " WHERE id = :categoryId" +
            " UNION ALL" +
            " SELECT c.id, c.title, c.parent_id" +
            " FROM product_categories c" +
            " JOIN product_categories_tree ct ON ct.id = c.parent_id" +
            ")" +
            "SELECT p.*" +
            "FROM products p" +
            " JOIN product_categories c ON p.category_id = c.id " +
            "WHERE c.id IN (SELECT id FROM product_categories_tree)",
            countQuery = "SELECT COUNT(*) FROM products",
            nativeQuery = true)
    Page<ProductEntity> findProductsByCategoryId(@Param("categoryId") short categoryId, Pageable pageable);


}
