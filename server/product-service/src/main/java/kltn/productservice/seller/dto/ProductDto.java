/*
 * Copyright (c) 2023 BAP IT CO., JSC. All Rights Reserved.
 *
 * BAP GROUP always aim to bring innovation to all products of our customers as well as our own.
 * Technology innovations are influencing life and contribute positive changes to the society,
 * in which BAP-ers always believe and aiming to
 *
 */package kltn.productservice.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

        private Long productId;
        private Long sellerId;
        private String title;
        private String sku;
        private Long categoryId;
        private BigDecimal price;
        private BigDecimal priceSales;
        private BigDecimal percentDiscount;
}
