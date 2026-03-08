package com.shop.onlineshopping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.onlineshopping.dto.UserDTO;
import com.shop.onlineshopping.entity.User;
import com.shop.onlineshopping.mapper.UserMapper;
import com.shop.onlineshopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(UserDTO userDTO) {
        // 1. 检查有没有这个人
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User existUser = userMapper.selectOne(wrapper);

        if (existUser != null) {
            throw new RuntimeException("注册失败：用户名已存在");
        }

        // 2. 如果没有，就开始保存
        User user = new User();
        user.setUsername(userDTO.getUsername());

        // ★★★ MD5 + Salt (加盐) 加密 ★★★
        String salt = "Shop2026_KUANG"; // 你的专属固定盐值
        String md5Password = DigestUtils.md5DigestAsHex((userDTO.getPassword() + salt).getBytes());
        user.setPassword(md5Password); // 存入数据库的是一串神仙都看不懂的乱码！

        user.setName(userDTO.getName());
        user.setRole(0); // 默认是普通用户
        user.setCreateTime(LocalDateTime.now()); // 设置当前时间

        user.setEmail(userDTO.getEmail());

        userMapper.insert(user);
    }

    // ↓↓↓↓↓ 新增的登录逻辑 ↓↓↓↓↓
    @Override
    public User login(UserDTO userDTO) {
        // 1. 根据用户名去数据库查
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User user = userMapper.selectOne(wrapper);

        // 2. 如果查不到
        if (user == null) {
            throw new RuntimeException("登录失败：账号不存在");
        }

        // 3. 如果查到了，比对密码
        // ★★★ 把前端传来的明文，加上同样的盐值，再MD5一次，然后去跟数据库比对 ★★★
        String salt = "Shop2026_KUANG";
        String inputMd5Password = DigestUtils.md5DigestAsHex((userDTO.getPassword() + salt).getBytes());

        if (!user.getPassword().equals(inputMd5Password)) {
            throw new RuntimeException("登录失败：密码错误");
        }

        // 4. 登录成功，返回用户信息
        return user;
    }
}