package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductRatingVisualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRatingVisualRepository extends JpaRepository<ProductRatingVisualEntity,Long> {
}
