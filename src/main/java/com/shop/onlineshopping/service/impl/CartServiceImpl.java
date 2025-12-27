package com.shop.onlineshopping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.onlineshopping.dto.OrderDTO;
import com.shop.onlineshopping.entity.Cart;
import com.shop.onlineshopping.entity.Product;
import com.shop.onlineshopping.mapper.CartMapper;
import com.shop.onlineshopping.mapper.ProductMapper;
import com.shop.onlineshopping.service.CartService;
import com.shop.onlineshopping.vo.CartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper; // 注入它，用来查商品详情

    @Override
    public void add(OrderDTO cartDTO) {
        // 1. 先查查车里有没有这个商品
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, cartDTO.getUserId());
        wrapper.eq(Cart::getProductId, cartDTO.getProductId());

        Cart existsCart = cartMapper.selectOne(wrapper);

        if (existsCart != null) {
            // 2. 如果有了，就加数量
            existsCart.setCount(existsCart.getCount() + cartDTO.getCount());
            cartMapper.updateById(existsCart);
        } else {
            // 3. 如果没有，就新增
            Cart cart = new Cart();
            cart.setUserId(cartDTO.getUserId());
            cart.setProductId(cartDTO.getProductId());
            cart.setCount(cartDTO.getCount());
            cart.setCreateTime(LocalDateTime.now());
            cartMapper.insert(cart);
        }
    }

    @Override
    public List<CartVO> myCart(Long userId) {
        // 1. 先查出购物车里的原始数据 (只包含 IDs)
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        List<Cart> cartList = cartMapper.selectList(wrapper);

        // 2. 准备一个空列表，用来装最后的结果
        List<CartVO> voList = new ArrayList<>();

        // 3. 遍历购物车，一个一个去查商品详情
        for (Cart cart : cartList) {
            CartVO vo = new CartVO();

            // 把 cart 里的基本属性 (id, userId, productId, count) 复制给 vo
            BeanUtils.copyProperties(cart, vo);

            // 4. 拿着 productId 去查商品表，获取名字、图片、价格
            Product product = productMapper.selectById(cart.getProductId());

            if (product != null) {
                // 把商品详情填进去
                vo.setProductName(product.getName());
                vo.setProductImage(product.getImage());
                vo.setPrice(product.getPrice());

                // 5. 顺便算一下这一行的小计金额 (单价 * 数量)
                BigDecimal total = product.getPrice().multiply(new BigDecimal(cart.getCount()));
                vo.setTotalMoney(total);
            }

            // 加到结果列表里
            voList.add(vo);
        }

        return voList;
    }

    @Override
    public void remove(Long id) {
        // 直接调用 MyBatis Plus 提供的删除方法
        cartMapper.deleteById(id);
    }

    @Override
    public void decrease(Long id) {
        Cart cart = cartMapper.selectById(id);
        if (cart != null) {
            if (cart.getCount() > 1) {
                // 1. 如果数量大于1，就减1
                cart.setCount(cart.getCount() - 1);
                cartMapper.updateById(cart);
            } else {
                // 2. 如果数量已经是1了，再减就没了，直接删除
                cartMapper.deleteById(id);
            }
        }
    }
}