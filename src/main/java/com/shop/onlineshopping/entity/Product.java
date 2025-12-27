package com.shop.onlineshopping.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product") // 对应数据库的 product 表
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String image;
    private String description;
    private BigDecimal price; // 价格用 BigDecimal
    private Integer stock;    // 库存
    private Integer status;   // 1上架 0下架

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}