# youmai-parent  优买
优买网上商城是一个综合性的 B2B2C 平台，类似京东商城、天猫商城。网站采用商家入驻的模式，商家入驻平台提交申请，有平台进行资质审核，审核通过后，商家拥有独立的管理后台录入商品信息。

## 品优购网上商城主要分为网站前台、运营商后台、商家管理后台三个子系统。
<br>

* ### 网站前台
> 主要包括网站首页、商家首页、商品详细页、、搜索页、会员中心、订单与支付相关页面、秒杀频道等

* ### 运营商后台
> 运营商后台是运营商的运营人员的管理后台。 主要包括商家审核、品牌管理、规格管理、模板管理、商品分类管理、商品审核、广告类型管理、广告管理、订单查询、商家结算等。

* ### 商家管理后台
> 入驻的商家进行管理的后台，主要功能是对商品的管理以及订单查询统计、资金结算等功能。

<br>
<br>

***
## 系统构架
> [SOA](https://zh.wikipedia.org/wiki/%E9%9D%A2%E5%90%91%E6%9C%8D%E5%8A%A1%E7%9A%84%E4%BD%93%E7%B3%BB%E7%BB%93%E6%9E%84)是是一种支持面向服务的架构样式。从服务、基于服务开发和服务的结果来看，面向服务是一种思考方式。
<br>

* ## 框架组合
> 后端框架采用Spring +SpringMVC+mybatis +[Dubbox](https://github.com/dangdangdotcom/dubbox) 。前端采用[angularJS](https://github.com/angular/angular.js) + [Bootstrap](https://github.com/twbs/bootstrap)。
* ## 工程结构
| 工程 | 说明 |  
|------------|------------ |  
|youmai-parent   | 聚合工程|  
|youmai-pojo     | 通用实体类层 |
|youmai-dao      | 通用数据访问层 |  
|youmai-xxxxx-interface |  某服务层接口 |   
|youmai-xxxxx-service   |  某服务层实现 |  
|youmai-xxxxx-web       |  某web工程  |




* ## 数据库表结构
|   表名称    |	 含义  |  
|------------|------------ |    
|tb_brand                |	品牌| 
|tb_specification        |	规格|  
|tb_specification_option |	规格选项|  
|tb_type_template	      | 类型模板：用于关联品牌和规格|  
|tb_item_cat 	         | 商品分类|  
|tb_seller               |	商家|  
|tb_goods                |	商品| 
|tb_goods_desc           |	商品详情|  
|tb_item                 |	商品明细|  
|tb_content	            | 内容（广告）|  
|tb_content_category     |	内容（广告）类型|  
|tb_user                 |	用户|  
|tb_order                |	订单|  
|tb_order_item	         |  订单明细|  
|tb_pay_log	            |  支付日志|  
