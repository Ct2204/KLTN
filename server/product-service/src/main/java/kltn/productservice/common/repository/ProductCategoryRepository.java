
package kltn.productservice.common.repository;


import kltn.productservice.common.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

     List<ProductCategoryEntity> findByParentId(short parent_id);
}