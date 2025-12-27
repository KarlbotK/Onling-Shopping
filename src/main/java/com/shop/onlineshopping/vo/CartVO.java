package com.shop.onlineshopping.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartVO {
    // 1. 购物车原本的信息
    private Long id;        // 购物车记录ID
    private Long userId;
    private Long productId;
    private Integer count;  // 买了几个
    private LocalDateTime createTime;

    // 2. 从商品表查出来的详细信息
    private String productName;  // 商品名
    private String productImage; // 图片
    private BigDecimal price;    // 单价
    private BigDecimal totalMoney; //这一行的小计金额 (单价 * 数量)
}