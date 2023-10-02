
package kltn.productservice.seller.repository;


import kltn.productservice.common.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<SellerEntity,Long> {
}
