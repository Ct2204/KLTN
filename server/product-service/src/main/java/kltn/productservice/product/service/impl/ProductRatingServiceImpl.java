

package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.ProductEntity;
import kltn.productservice.common.entity.ProductRatingEntity;
import kltn.productservice.common.entity.ProductRatingVisualEntity;
import kltn.productservice.common.exception.ResourceNotFoundException;
import kltn.productservice.common.repository.ProductRatingRepository;
import kltn.productservice.common.repository.ProductRatingVisualRepository;
import kltn.productservice.common.repository.ProductRepository;
import kltn.productservice.common.vo.ProductRatingVisualType;
import kltn.productservice.product.dto.PageRatingResDto;
import kltn.productservice.product.dto.ProductRatingReqDto;
import kltn.productservice.product.dto.UserDto;
import kltn.productservice.product.converter.ProductRatingConverter;
import kltn.productservice.product.service.ProductRatingService;
import kltn.productservice.product.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductRatingServiceImpl implements ProductRatingService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Autowired
    private ProductRatingVisualRepository productRatingVisualRepository;

    @Autowired
    private ProductRatingConverter productRatingConverter;

    @Autowired
    private StorageService storageService;


    @Autowired
    private RestTemplate template;

    @Value("${image.dir.rating}")
    private String imageDirLocation;

    @Value("${video.dir.rating}")
    private String videoDirLocation;

    @Value("${bap.ecommerce.domain}")
    private String domain;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RestTemplate restTemplate;



    /**
     *
     * Get all ProductRating with paginate by product id
     *
     * @param perPage this is the number of elements per page .
     * @param productId This is an ID of exist product.
     * @param currentPage This is current page.
     * @return list rating of page and paginate information.
     */
    @Override
    public PageRatingResDto findAllProductRatingByProductId(Long productId,int perPage, int currentPage)
                                                            throws IllegalArgumentException {

        Pageable paging = PageRequest.of(currentPage,
                perPage, Sort.by("createdAt").descending());

        ProductEntity product = this.productRepository.findOneById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Page<ProductRatingEntity> pageListEntity
                = this.productRatingRepository.findByProduct(product, paging);

        return pageListEntity.getTotalElements() > 0
                ? new PageRatingResDto(pageListEntity.getTotalPages(),
                pageListEntity.getTotalElements(),
                pageListEntity.getSize(),
                pageListEntity.getNumberOfElements(),
                pageListEntity.getNumber() + 1,
                pageListEntity.isFirst(),
                pageListEntity.isLast(),
                productId,
                // Calculate average number of stars and round 1 number after decimal point
                ((float) Math.round((float)
                        this.productRatingRepository.sumStarOfProduct(productId)
                        / pageListEntity.getTotalElements() * 10)) / 10,
                pageListEntity.getContent().stream()
                        .map(item -> this.productRatingConverter.toDto(item))
                        .collect(Collectors.toList()))
                : null;
    }

    /**
     *
     * Insert new rating for a product
     * @param userId This is id of current user.
     * @param productRating This is all information to insert new rating.
     * @throws IllegalArgumentException, ResourceNotFoundException
     */
    @Override
    @Transactional
    public void saveProductRating(Long userId, ProductRatingReqDto productRating) {

        List<MultipartFile> files = productRating.getListMedia();

        ProductEntity product = this.productRepository.findOneById(productRating.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        long userID = getUser(userId);

        ProductRatingEntity productRatingEntity = this.productRatingConverter.toEntity(productRating);
        productRatingEntity.setProduct(product);
        productRatingEntity.setUserId(userID);

        // Save rating
        this.productRatingRepository.save(productRatingEntity);

        // Check if the rating has images and video, then save a list of them
        if ( files!=null) {

            for (MultipartFile file : files) {
                if (!this.storageService.isImageFile(file) && !this.storageService.isVideoFile(file)) {
                    throw new IllegalArgumentException("Only store image and video file");
                }
            }
            for (MultipartFile file : files) {

                ProductRatingVisualEntity productRatingVisualEntity = new ProductRatingVisualEntity();
                productRatingVisualEntity.setProduct(product);
                productRatingVisualEntity.setRating(productRatingEntity);
                // Check the file type (image or video) and save it to the correct folder
                if (this.storageService.isImageFile(file)) {
                    productRatingVisualEntity.setType((short) ProductRatingVisualType.IMAGE.ordinal());
                    productRatingVisualEntity.setUrl(domain + "/images/rating/"
                            + this.storageService.storeFile(file, Paths.get(imageDirLocation)));
                } else {
                    productRatingVisualEntity.setType((short) ProductRatingVisualType.VIDEO.ordinal());
                    productRatingVisualEntity.setUrl(domain + "/videos/rating/"
                            + this.storageService.storeFile(file, Paths.get(videoDirLocation)));
                }

                this.productRatingVisualRepository.save(productRatingVisualEntity);
            }
        }
    }

    @Override
    public Long getUser(Long userId) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        try{
//            ResponseEntity a = (ResponseEntity) this.restTemplate.getForObject("http://authentication-service/api/v1/users/1/rating",
//                    Object.class);
//        }catch (Exception e){
//            ObjectMapper objectMapper = new ObjectMapper();
//            ResponseDto data = objectMapper.readValue(e.getMessage().substring(7,e.getMessage().length()-1).replace("<EOL> ",""), ResponseDto.class);
//            return ResponseEntity.status(data.getCode()).body(data);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(null);
        Long user = this.restTemplate.exchange("http://user-service/api/v1/users/"+userId+"/rating",HttpMethod.GET,entity ,UserDto.class).getBody().getUserId();
        return user;



    }
}
