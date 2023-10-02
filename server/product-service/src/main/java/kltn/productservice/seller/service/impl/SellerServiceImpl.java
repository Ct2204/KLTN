
package kltn.productservice.seller.service.impl;


import kltn.productservice.common.entity.*;
import kltn.productservice.common.repository.*;
import kltn.productservice.seller.dto.*;
import kltn.productservice.common.exception.InternalServerErrorException;
import kltn.productservice.common.exception.ResourceNotFoundException;
import kltn.productservice.common.vo.ProductStatusType;
import kltn.productservice.seller.repository.SellerRepository;
import kltn.productservice.seller.service.SellerService;
import kltn.productservice.seller.service.SlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductOptionDetailRepository productOptionDetailRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductItemDetailRepository productItemDetailRepository;

    @Autowired
    private ProductVisualRepository productVisualRepository;

    @Autowired
    private ProductDescriptionVisualRepository productDescriptionVisualRepository;

    @Autowired
    private ProductDescriptionRepository productDescriptionRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private SlugService slugService;

    private static final int LIMIT_OPTION_SIZE  = 3;

    private static final String IMAGE_PRODUCT_PATH = "/images/product";
    private static final String VIDEO_PRODUCT_PATH = "/videos/product";
    @Value("${image.dir.product}")
    private String dirImageLocation;

    @Value("${video.dir.product}")
    private String dirVideoLocation;

    @Value("${bap.ecommerce.domain}")
    private String domain;

    /**
     * Handle save a new product.
     *
     * @param productDto This is a DTO contains information of the product.
     * @throws ResourceNotFoundException if the ID of the product or seller not found.
     * @throws IllegalArgumentException if the product doesn't belong to the seller.
     */
    @Transactional
    @Override
    public Long saveProduct(ProductDto productDto) {

        Long sellerId = productDto.getSellerId();
        SellerEntity seller = this.sellerRepository
                .findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " +sellerId+" not found!"));

        Long categoryId = productDto.getCategoryId();
        ProductCategoryEntity category = null;
        if(categoryId != null){
            category = this.productCategoryRepository
                    .findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id " +categoryId+" not found!"));
        }
        //Handle saving product
        ProductEntity product = this.mapperProductDtoToEntity(productDto,seller,category);
        product.setCreatedAt(Instant.now());
        product.setStatus(ProductStatusType.DRAFT);
        product = this.productRepository.save(product);
        return product.getId();
    }

    /**
     * Handle update Product.
     *
     * @param productDto This is a DTO contains information of the product.
     * @throws ResourceNotFoundException if the ID of the product or seller not found.
     * @throws IllegalArgumentException if the product doesn't belong to the seller.
     */
    @Transactional
    @Override
    public void updateProduct(ProductDto productDto) {
        Long productId = productDto.getProductId();
        ProductEntity product = this.productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +productId+" not found!"));
        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +productId+" not found!");
        }
        Long sellerId = productDto.getSellerId();
        SellerEntity seller = this.sellerRepository
                .findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " +sellerId+" not found!"));

        Long categoryId = productDto.getCategoryId();
        ProductCategoryEntity category = this.productCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " +categoryId+" not found!"));

        //Handle saving product
        ProductEntity productEntity = this.mapperProductDtoToEntity(productDto,seller,category);
        productEntity.setId(productId);
        productEntity.setStatus(product.getStatus());
        productEntity.setCreatedAt(product.getCreatedAt());
        productEntity.setUpdatedAt(Instant.now());
        this.productRepository.save(productEntity);
    }

    /**
     * Handle save ProductDetail.
     *
     * @param productDetailsDto This is a DTO contains a list detail of the product.
     * @throws ResourceNotFoundException if the ID of the product or seller not found.
     * @throws IllegalArgumentException if the product doesn't belong to the seller.
     */
    @Transactional
    @Override
    public void saveProductDetail(ProductDetailsDto productDetailsDto){
        Long productId = productDetailsDto.getProductId();
        ProductEntity product = this.productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +productId+" not found!"));

        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +productId+" not found!");
        }
        Long sellerId = productDetailsDto.getSellerId();
        SellerEntity seller = this.sellerRepository
                .findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " +sellerId+" not found!"));

        if(product.getSeller().getId().longValue() != sellerId.longValue()){
            throw new IllegalArgumentException("Wrong product id or seller id!");
        }

        this.productDetailRepository.deleteAllByProductId(productId);

        List<ProductDetailDto> productDetails = productDetailsDto.getProductDetails();
        if(!productDetails.isEmpty()){
            for(ProductDetailDto productDetailDto : productDetails){
                ProductDetailEntity productDetail = new ProductDetailEntity();
                productDetail.setProduct(product);
                productDetail.setKey(productDetailDto.getKey());
                productDetail.setValue(productDetailDto.getValue());
                this.productDetailRepository.save(productDetail);
            }
        }
    }

    /**
     * Handle save ProductDescription and its images.
     *
     * @param productDescriptionDto This is a DTO contains description and images of the product.
     * @throws ResourceNotFoundException if the ID of the product or seller not found.
     * @throws IllegalArgumentException if the product doesn't belong to the seller.
     */
    @Transactional
    @Override
    public void saveProductDescription(ProductDescriptionDto productDescriptionDto){

        Long productId = productDescriptionDto.getProductId();
        ProductEntity product = this.productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +productId+" not found!"));

        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +productId+" not found!");
        }
        Long sellerId = productDescriptionDto.getSellerId();
        SellerEntity seller = this.sellerRepository
                .findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " +sellerId+" not found!"));

        if(product.getSeller().getId().longValue() != sellerId.longValue()){
            throw new IllegalArgumentException("Wrong product id or seller id!");
        }

        this.productDescriptionVisualRepository.deleteAllByProductId(productId);
        this.productDescriptionRepository.deleteAllByProductId(productId);

        ProductDescriptionEntity description = new ProductDescriptionEntity();
        description.setProduct(product);
        description.setDescriptions(productDescriptionDto.getDescription());
        description.setCreatedAt(Instant.now());
        description = this.productDescriptionRepository.save(description);
        List<ProductDescriptionVisualDto> images = productDescriptionDto.getImages();

        if(!images.isEmpty()){

            // Saving ProductDescriptionVisual
            for(ProductDescriptionVisualDto image : images){
                ProductDescriptionVisualEntity visual = new ProductDescriptionVisualEntity();
                visual.setUrl(image.getUrl());
                visual.setType(image.getType());
                visual.setProduct(product);
                visual.setProductDescription(description);
                visual.setCreatedAt(Instant.now());
                this.productDescriptionVisualRepository.save(visual);
            }
        }
    }

    /**
     * Handle save a list of option and product item.
     *
     * @param productOptionWithItemDto This is a DTO contains list ProductOptionDTO with ProductItemDTO.
     * @throws ResourceNotFoundException if the ID of the producr or seller not found.
     * @throws IllegalArgumentException if the size of list option is more than 3.
     */
    @Transactional
    @Override
    public void saveProductOptionWithItem(ProductOptionWithItemDto productOptionWithItemDto) {

        Long productId = productOptionWithItemDto.getProductId();
        ProductEntity product = this.productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +productId+" not found!"));

        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +productId+" not found!");
        }
        Long sellerId = productOptionWithItemDto.getSellerId();
        SellerEntity seller = this.sellerRepository
                .findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " +sellerId+" not found!"));

        if(product.getSeller().getId().longValue() != sellerId.longValue()){
            throw new IllegalArgumentException("Wrong product id or seller id!");
        }

        //Option
        List<ProductOptionDto> options = productOptionWithItemDto.getOptions();

        if(options.size() > this.LIMIT_OPTION_SIZE){
            throw new IllegalArgumentException("The limit size option is 3!");
        }

        //Delete all product item details by product id
        this.productItemDetailRepository.deleteAllByProductId(productId);

        //Delete all product visuals details by product id
        this.productVisualRepository.deleteAllByProductId(productId);

        //Delete all product option details by product id
        this.productOptionDetailRepository.deleteAllByProductId(productId);

        //Delete all product options by product id
        this.productOptionRepository.deleteAllByProductId(productId);

        List<ProductItemDto> productItems = productOptionWithItemDto.getProductItems();

        List<ProductOptionEntity> listProductOptions = new ArrayList<>();

        //Handle option
        if(!options.isEmpty()){
            listProductOptions = this.mapperListProductOptionDtoToEntity(options,product);
        }

        int totalOption = options.size();
        List<List<ProductOptionDetailEntity>> allOption = new ArrayList<>();
        for(int i = 0; i < totalOption; i++){
            allOption.add(listProductOptions.get(i).getProductOptionDetails());
        }

        //List ids of ProductItemEntities need to be checked
        List<Long> productItemId = productItems
                .stream()
                .filter(p -> p.getId() != null)
                .map(p -> p.getId())
                .collect(Collectors.toList());


        //List ids of ProductItemEntities need to be checked
        List<ProductItemDto> productItemDtoWithId = productItems
                .stream()
                .filter(p -> p.getId() != null)
                .collect(Collectors.toList());


        //Get list ProductItemEntity by their ID
        List<ProductItemEntity> productItemEntityWithId = this.productItemRepository
                .getAllByListIds(productItemId,productId);

        //Sorting list of product items in order of list of ids
        productItemEntityWithId.sort(Comparator.comparingLong(p -> productItemId.indexOf(p.getId())));

        int totalProductItemWithId =  productItemEntityWithId.size();

        // If size of list ProductItem from database has ID is matched with size of list ProductItem from DTO
        if(productItemDtoWithId != null && productItemDtoWithId.size() != totalProductItemWithId){
            throw new ResourceNotFoundException("Some items don't exist!");
        }
        //Update status of all product item not in DTO into 0 (delete product item which not in used)
        if(!productItemDtoWithId.isEmpty()){
            this.productItemRepository.updateProductItemByStatus(productItemId,productId);
        }else{
            System.out.println("ID: " +productId);
            this.productItemRepository.updateAllProductItemByStatus(productId);
        }
        //Handle saving list product item have id
        this.handleSavingListProductItemHaveId(productItemDtoWithId,product,allOption);

        //List ids of ProductItemEntities need to be checked
        List<ProductItemDto> productItemDtoWithNoId = productItems
                .stream()
                .filter(p -> p.getId() == null)
                .collect(Collectors.toList());

        if(productItemDtoWithNoId.size() > 0){
            //Handle saving list product item have no id
            this.handleSavingListProductItemHaveNoId(productItemDtoWithNoId,product,allOption);
        }
    }

    /**
     * Handle upload file of the product.
     *
     * @param file This is a DTO contains list ProductOptionDTO with ProductItemDTO.
     * @throws IllegalArgumentException if file is null.
     * @throws InternalServerErrorException if uploading is failed.
     */
    @Override
    public String uploadProductFile(MultipartFile file) {
        String decode = this.sha256(String.valueOf(System.currentTimeMillis()));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file to upload!");
        }

        try {
            // Generate a unique file name to avoid conflicts
            int lastDotIndex = file.getOriginalFilename().toString().lastIndexOf(".");
            String fileNameDecoded = decode + "." + file
                    .getOriginalFilename()
                    .toString()
                    .substring(lastDotIndex + 1);
            String dirLocation = null;
            String filePath = null;
            // Check if it's an image
            if (file.getContentType().startsWith("image/")) {
                // It's an image
                dirLocation = this.dirImageLocation;
                filePath = this.IMAGE_PRODUCT_PATH;
            }

            // Check if it's a video
            if (file.getContentType().startsWith("video/")) {
                // It's a video
                dirLocation = this.dirVideoLocation;
                filePath = this.VIDEO_PRODUCT_PATH;
            }

            // Get the resource folder path
            Path resourcePath = Paths.get(dirLocation);

            // Formatting the path
            Path destinationFilePath = resourcePath
                    .resolve(Paths.get(fileNameDecoded))
                    .normalize();

            // Save the file to the specified path
            Files.copy(file.getInputStream(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            return domain + filePath + "/" + fileNameDecoded;
        } catch (Exception e) {
            // Handle file upload failure
            throw new InternalServerErrorException("Failed to upload the file!");
        }
    }

    /**
     * Handle get a list of option and product item.
     *
     * @param id This is ID of the product.
     * @return ProductOptionWithItemDto contains list of option and product item.
     * @throws ResourceNotFoundException if the ID of the product not found.
     */
    @Override
    public ProductOptionWithItemDto getProductOptionWithItem(Long id) {
        ProductEntity product = this.productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +id+" not found!"));

        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +id+" not found!");
        }
        // List ProductOption wit order ASC
        List<ProductOptionEntity> listProductOption = this.productOptionRepository.getAllByProductIdInAscOrder(id);
        // List ProductItem wit order ASC
        List<ProductItemEntity> listProductItem = this.productItemRepository.getAllByProductIdInAscOrder(id);

        //ID of option detail : Index of option detail
        Map<Long,Integer> map = new HashMap<>();

        //Handle option
        List<ProductOptionDto> listOptionDto = new ArrayList<>();
        if(!listProductOption.isEmpty()){
            int n = listProductOption.size();
            List<ProductOptionDetailEntity> listOptionDetail;
            List<ProductVisualEntity> listProductVisual;

            // Iterate to map ProductOption from Entity to DTO
            for( int i = 0; i < n; i++){ // 3 or 2 Option

                ProductOptionDto option = new ProductOptionDto();
                option.setName(listProductOption.get(i).getOption());

                // Contains name of ProductOptionDetail
                List<String> listValue = new ArrayList<>();
                // List of ProductOptionDetail by id of ProductOption
                listOptionDetail = this.productOptionDetailRepository
                        .getAllByProductIdInAscOrder(listProductOption.get(i).getId());

                //List images of ProductOptionDetail
                List<List<ProductOptionDetailImageDto>> listImagesOption = new ArrayList<>();

                //Iterate list ProductOptionDetail to put its ID as key and its index as value in HashMap
                for(int j = 0 ; j < listOptionDetail.size(); j++){
                    // Key id option detail, value : index of optiondetail
                    map.put(listOptionDetail.get(j).getId(),j);

                    // List ProductVisual by ProductOptionDetail ID
                    listProductVisual = this.productVisualRepository
                            .getAllByProductOptionDetailIdInAscOrder(listOptionDetail.get(j).getId());

                    List<ProductOptionDetailImageDto> listImagesOptionDetail = new ArrayList<>();

                    if(!listProductVisual.isEmpty()){
                        for(ProductVisualEntity visual : listProductVisual){
                            ProductOptionDetailImageDto image = new ProductOptionDetailImageDto();
                            image.setUrl(visual.getUrl());
                            image.setType(visual.getType());
                            listImagesOptionDetail.add(image);
                        }

                    }
                    listImagesOption.add(listImagesOptionDetail);
                    listValue.add(listOptionDetail.get(j).getValue());
                    option.setValues(listValue);
                    option.setImages(listImagesOption);
                }
                listOptionDto.add(option);
            }
        }

        List<ProductItemDto> listItemDto = new ArrayList<>();
        if(!listProductItem.isEmpty()){
            List<ProductItemDetailEntity> listProductItemDetail;

            //Iterate a list of ProductItem to map from Entity to DTO
            for(ProductItemEntity item : listProductItem){

                listProductItemDetail = this.productItemDetailRepository
                        .getAllByProductItemId(item.getId());

                // This list contains the indexes of ProductOptionDetail from the list contains ProductOptionDetail
                // By using ID ProductOptionDetail in ProductItemDetail,
                // we can track the index of the ProductOptionDetail that is stored in the HashMap
                List<Integer> listOptionIndex = new ArrayList<>();
                if(!map.isEmpty()){
                    for(ProductItemDetailEntity itemDetail : item.getProductItemDetails()){
                        listOptionIndex.add(map.get(itemDetail.getProductOptionDetail().getId()));
                    }
                }
                ProductItemDto dto = this.mapperProductItemEntityToDto(item,listOptionIndex);
                listItemDto.add(dto);
            }
        }
        return this.mapperProductEntityToDto(product,listOptionDto,listItemDto);
    }

    /**
     * Handle get a ProductDescription.
     *
     * @param id This is ID of the product.
     * @return The description of the product.
     * @throws ResourceNotFoundException if the ID of the product not found.
     */
    @Override
    public ProductDescriptionDto getProductDescription(Long id) {
        ProductEntity product = this.productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +id+" not found!"));

        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +id+" not found!");
        }
        ProductDescriptionDto dto = new ProductDescriptionDto();

        if(!product.getProductDescriptions().isEmpty()){
            dto.setDescription(product.getProductDescriptions().get(0).getDescriptions());
        }
        dto.setProductId(id);
        dto.setSellerId(product.getSeller().getId());
        List<ProductDescriptionVisualEntity> visuals = this.productDescriptionVisualRepository
                .getAllByProductIdInAscOrder(id);
        List<ProductDescriptionVisualDto> images = new ArrayList<>();
        if(!visuals.isEmpty()){
            for(ProductDescriptionVisualEntity visual : visuals){
                ProductDescriptionVisualDto image = new ProductDescriptionVisualDto();
                image.setUrl(visual.getUrl());
                image.setType(visual.getType());
                images.add(image);
            }
        }
        dto.setImages(images);
        return dto;
    }

    /**
     * Handle get a Product.
     *
     * @param id This is ID of the product.
     * @return a product.
     * @throws ResourceNotFoundException if the ID of the product not found.
     */
    @Override
    public ProductDto getProduct(Long id) {
        ProductEntity product = this.productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +id+" not found!"));
        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +id+" not found!");
        }
        ProductDto dto = new ProductDto();
        dto.setProductId(id);
        dto.setSellerId(product.getSeller().getId());
        dto.setTitle(product.getTitle());
        dto.setSku(product.getSku());
        dto.setCategoryId(product.getCategory() == null ? null : product.getCategory().getId());
        dto.setPrice(product.getPrice());
        dto.setPriceSales(product.getPriceSales());
        dto.setPercentDiscount(product.getPercentDiscount());
        return dto;
    }

    /**
     * Handle get a list of ProductDetail.
     *
     * @param id This is ID of the product.
     * @return ProductDetailsDto contains a list detail of the product.
     * @throws ResourceNotFoundException if the ID of the product not found.
     */
    @Override
    public ProductDetailsDto getProductDetails(Long id) {
        ProductEntity product = this.productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +id+" not found!"));
        if(!isAvailable(product)){
            throw new ResourceNotFoundException("Product with id " +id+" not found!");
        }
        ProductDetailsDto dto = new ProductDetailsDto();
        dto.setProductId(id);
        dto.setSellerId(product.getSeller().getId());
        List<ProductDetailEntity> productDetails = this.productDetailRepository.getAllByProductIdInAscOrder(id);
        List<ProductDetailDto> productDetailsDto = new ArrayList<>();
        for(ProductDetailEntity productDetail :  productDetails){
            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setKey(productDetail.getKey());
            productDetailDto.setValue(productDetail.getValue());
            productDetailsDto.add(productDetailDto);
        }
        dto.setProductDetails(productDetailsDto);
        return dto;
    }

    /**
     * Handle save a list of Product Item from DTO to database.
     *
     * @param productItemDtoWithNoId This is a list of Product Item which each item has no ID from DTO(New Product Item).
     * @param product This is a product.
     * @param allOption This is a list of option to map with Product Item.
     */
    public void handleSavingListProductItemHaveNoId(List<ProductItemDto> productItemDtoWithNoId,
                                                    ProductEntity product,
                                                    List<List<ProductOptionDetailEntity>> allOption){
        ProductItemEntity productItemEntityNoId;
        // Iterate to map Product Item from DTO to Entity
        for(ProductItemDto productItemDto : productItemDtoWithNoId){
            productItemEntityNoId = this.mapperProductItemDtoToEntity(productItemDto,product);
            productItemEntityNoId = this.productItemRepository.save(productItemEntityNoId);

            // Iterate to map Product Item Detail from DTO to Entity
            // allOption contains a list of list option and each list option has a combo option of 1 Product Item
            for(int i = 0; i < allOption.size(); i++){
                ProductItemDetailEntity productItemDetailEntity = new ProductItemDetailEntity();
                productItemDetailEntity.setProductItem(productItemEntityNoId);
                productItemDetailEntity.setProductOptionDetail(
                        allOption.get(i)
                                .get(productItemDto.getOptionValueIndex().get(i)));
                this.productItemDetailRepository.save(productItemDetailEntity);
            }
        }
    }

    /**
     * Handle save a list of Product Item from DTO to database.
     *
     * @param productItemDtoWithId This is a list of Product Item which each item has ID from DTO(Old Product Item).
     * @param product This is a product.
     * @param allOption This is a list of option to map with Product Item.
     */
    public void handleSavingListProductItemHaveId(List<ProductItemDto> productItemDtoWithId,
                                                  ProductEntity product,
                                                  List<List<ProductOptionDetailEntity>> allOption){
        ProductItemEntity productItemEntity;

        // Iterate to map Product Item from DTO to Entity
        for(int i = 0; i < productItemDtoWithId.size(); i++){
            productItemEntity = this.mapperProductItemDtoToEntity(productItemDtoWithId.get(i),product);
            productItemEntity.setId(productItemDtoWithId.get(i).getId());
            productItemEntity = this.productItemRepository.save(productItemEntity);

            // Iterate to map Product Item Detail from DTO to Entity
            // allOption contains a list of list option and each list option has a combo option of 1 Product Item
            for(int j = 0; j < allOption.size(); j++){
                ProductItemDetailEntity productItemDetailEntity = new ProductItemDetailEntity();
                productItemDetailEntity.setProductItem(productItemEntity);
                productItemDetailEntity.setProductOptionDetail(
                        allOption.get(j)
                                .get(productItemDtoWithId.get(i).getOptionValueIndex().get(j)));

                this.productItemDetailRepository.save(productItemDetailEntity);
            }
        }
    }


    /**
     * Map Product Option from DTO to Entity.
     *
     * @param options This is a list of option DTO.
     * @param product This is a product .
     * @return a list of ProductOptionEntity.
     */
    public List<ProductOptionEntity> mapperListProductOptionDtoToEntity(List<ProductOptionDto> options,
                                                                        ProductEntity product){
        List<ProductOptionEntity> listProductOptions = new ArrayList<>();
        for(ProductOptionDto optionDto : options){
            ProductOptionEntity option = this.mapperProductOptionDtoToEntity(optionDto,product);
            //Iterate a list of its values to create a list of product option detail
            for(int i = 0; i < optionDto.getValues().size(); i++){
                ProductOptionDetailEntity optionDetail =
                        this.mapperProductOptionDetailDtoToEntity(optionDto.getValues().get(i),option,product);

                //If the option detail has any image
                if(optionDto.getImages().size() > 0){
                    if(optionDto.getImages().get(i).size() > 0){

                        List<ProductOptionDetailImageDto> images = optionDto.getImages().get(i);
                        // Create visual of the option detail
                        for(int j = 0; j < images.size(); j++){
                            this.mapperProductVisualDtoToEntity(images.get(j),optionDetail,product);
                        }
                    }
                }
                // Add optionDetail into the list option details of the option
                option.getProductOptionDetails().add(optionDetail);
            }

            //Add option into list
            listProductOptions.add(option);
        }
        return listProductOptions;
    }

    /**
     * Map Product from DTO to Entity.
     *
     * @param productDto This is a product DTO.
     * @param seller This is a seller of the product .
     * @param category This is a category of the product.
     * @return ProductEntity.
     */
    public ProductEntity mapperProductDtoToEntity(ProductDto productDto,
                                                  SellerEntity seller,
                                                  ProductCategoryEntity category){
        ProductEntity product = new ProductEntity();
        product.setSeller(seller);
        product.setTitle(productDto.getTitle());
        product.setSku(productDto.getSku());
        product.setCategory(category);
        product.setPrice(productDto.getPrice());
        product.setPriceSales(productDto.getPriceSales());
        product.setPercentDiscount(productDto.getPercentDiscount());
        if(product.getTitle() != null){
            product.setSlug(slugService.createSlug(product.getTitle()));
        }
        return product;
    }

    /**
     * Combine Product Option with Product Item DTO.
     *
     * @param product This is a product entity.
     * @param listOptionDto This is a list of product option DTO.
     * @param listItemDto This is a list of product item DTO.
     * @return ProductOption with ProductItem DTO.
     */
    public ProductOptionWithItemDto mapperProductEntityToDto(ProductEntity product,
                                                             List<ProductOptionDto> listOptionDto,
                                                             List<ProductItemDto> listItemDto){
        ProductOptionWithItemDto productDto = new ProductOptionWithItemDto();
        productDto.setProductId(product.getId());
        productDto.setSellerId(product.getSeller().getId());
        productDto.setOptions(listOptionDto);
        productDto.setProductItems(listItemDto);
        return productDto;
    }

    /**
     * Map Product Item from Entity to DTO.
     *
     * @param item This is a product item entity.
     * @param listOptionIndex This is a list index of item correspond with product option detail .
     * @return ProductItemDTO.
     */
    public ProductItemDto mapperProductItemEntityToDto(ProductItemEntity item,
                                                          List<Integer> listOptionIndex){
        ProductItemDto dto = new ProductItemDto();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setOptionValueIndex(listOptionIndex);
        return dto;
    }

    /**
     * Map Product Item from DTO to Entity.
     *
     * @param productItemDto This is a product item from DTO.
     * @param product This is a product.
     * @return ProductItemEntity.
     */
    public ProductItemEntity mapperProductItemDtoToEntity(ProductItemDto productItemDto,
                                                          ProductEntity product){
        ProductItemEntity productItemEntity = new ProductItemEntity();
        productItemEntity.setTitle(productItemDto.getTitle());
        productItemEntity.setProduct(product);
        productItemEntity.setPrice(productItemDto.getPrice());
        productItemEntity.setQuantity(productItemDto.getQuantity());
        productItemEntity.setStatus(true);
        return productItemEntity;
    }


    /**
     * Map Product Option from DTO to Entity.
     *
     * @param optionDto This is a product option from DTO.
     * @param product This is a product.
     * @return ProductOptionEntity.
     */
    public ProductOptionEntity mapperProductOptionDtoToEntity(ProductOptionDto optionDto,
                                                        ProductEntity product){
        ProductOptionEntity option = new ProductOptionEntity();
        option.setProduct(product);
        option.setOption(optionDto.getName());
        option.setCreatedAt(Instant.now());
        return this.productOptionRepository.save(option);
    }

    /**
     * Map Product Option Detail from DTO to Entity.
     *
     * @param value This is the value of product option detail.
     * @param option This is the option of that option detail.
     * @param product This is a product.
     * @return ProductOptionDetailEntity.
     */
    public ProductOptionDetailEntity mapperProductOptionDetailDtoToEntity(String value,
                                                              ProductOptionEntity option,
                                                              ProductEntity product){
        ProductOptionDetailEntity optionDetail = new ProductOptionDetailEntity();
        optionDetail.setProduct(product);
        optionDetail.setValue(value);
        optionDetail.setProductOption(option);
        optionDetail.setCreatedAt(Instant.now());
        return this.productOptionDetailRepository.save(optionDetail);
    }



    /**
     * Map Product Visual from DTO to Entity.
     *
     * @param image This is an image from product option detail DTO.
     * @param optionDetail This is a product option detail DTO.
     * @param product This is a product.
     * @return ProductVisualEntity.
     */
    public ProductVisualEntity mapperProductVisualDtoToEntity(ProductOptionDetailImageDto image,
                                                              ProductOptionDetailEntity optionDetail,
                                                              ProductEntity product){
        ProductVisualEntity visual = new ProductVisualEntity();
        visual.setUrl(image.getUrl());
        visual.setType(image.getType());
        visual.setProduct(product);
        visual.setProductOptionDetail(optionDetail);
        return this.productVisualRepository.save(visual);
    }

    /**
     * Check Product Status.
     *
     * @param product This is a product need to be checked.
     * @return true if product is available.
     */
    public boolean isAvailable(ProductEntity product){
        if((product.getStatus() == ProductStatusType.DELETED) || (product.getStatus() == ProductStatusType.RECYCLE)){
            return false;
        }
        return true;
    }

    /**
     * Hash name.
     *
     * @param base This is string need to be coded.
     * @return Coded name.
     */
    public String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
