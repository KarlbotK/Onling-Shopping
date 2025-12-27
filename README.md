# 在线商城系统 (Online Shopping System)

## 👤 作者信息
* **学号**：202330452541
* **姓名**：邝伟健

## 📝 项目简介
这是一个基于 **Spring Boot** + **MyBatis Plus** + **HTML/JS** 开发的前后端分离电商系统。
实现了用户注册登录、商品浏览、购物车管理、订单结算、模拟支付、邮件通知以及管理员后台（商品管理、发货、销售统计）等核心功能。
本项目支持本地开发调试，并可打包部署至云服务器运行。

## 📂 源代码结构说明

本项目采用标准的 Maven 项目结构，核心代码位于 `src/main/java/com/shop/onlineshopping` 包下：

### 1. 核心控制层 (Controller)
* `controller/UserController.java`: 处理用户注册、登录请求。
* `controller/ProductController.java`: 处理商品的查询（顾客用）及增删改（管理员用）。
* `controller/CartController.java`: 处理购物车的添加、查看、删除、数量调整。
* `controller/OrdersController.java`: 处理下单、结账、支付、发货及销售统计。

### 2. 业务逻辑层 (Service)
* `service/impl/UserServiceImpl.java`: 实现用户注册（含查重）、登录逻辑。
* `service/impl/ProductServiceImpl.java`: 实现商品的上下架、库存管理逻辑。
* `service/impl/CartServiceImpl.java`: 实现购物车逻辑，包含商品详情的关联查询。
* `service/impl/OrdersServiceImpl.java`: **核心业务类**。实现了订单创建、库存扣减、事务控制、以及发货时的邮件发送逻辑。
* `service/EmailService.java`: 封装了 JavaMailSender，用于发送发货通知邮件。

### 3. 数据访问层 (Mapper)
* `mapper/`: 包含 `UserMapper`, `ProductMapper`, `OrdersMapper`, `CartMapper`。
* 基于 MyBatis Plus，主要负责与 MySQL 数据库交互。
* `OrdersMapper` 中包含自定义 SQL 用于生成销售统计报表。

### 4. 实体与模型 (Entity/DTO/VO)
* `entity/`: 对应数据库表的实体类 (`User`, `Product`, `Orders`, `Cart`)。
* `dto/`: 数据传输对象，用于接收前端参数 (如 `UserDTO`, `OrderDTO`)。
* `vo/`: 视图对象，用于返回给前端展示的数据 (如 `OrderVO`, `CartVO`, `SalesVO`)。

### 5. 公共组件
* `common/Result.java`: 统一的 API 返回结果封装类 (Code, Msg, Data)。

### 6. 前端资源
* `src/main/resources/static/index.html`: **单页应用 (SPA)**。包含顾客商城、购物车、订单中心以及管理员后台的所有界面逻辑，使用原生 JS + Fetch API 与后端交互。

### 7. 配置文件
* `src/main/resources/application.properties`: 包含数据库连接配置、MyBatis 日志配置及 SMTP 邮件服务器配置。

---
