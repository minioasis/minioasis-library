# minioasis-library
Minioasis Library Management System

https://wp.me/p7WqoV-1rF

小绿洲图书馆管理系统是为了解决微型图书馆日常作业而开发的开源软件（Apache License 2.0）,
主要的对象为民办、小学、中学和私人图书馆。

A) 能够解决的问题：

* 编目 Cataloging (简单编目，不用MARC 格式)、网上抓取编目资料 Web Scraping
* 记录读者资料 Patron Records
* 借还书 Circulation
* 简单的 在线公共访问目录 OPAC （Online public access catalog）
* 用户界面 – 续借、预约、检查自己的账户以往的借书记录 borrowing history
* 简单打印索书号 Shelfmark Printing
* 库存清点 Stock Check
* 简单报告 Simple Reports
* 多语言界面（英文、中文、马来文）
* 网页界面，所以允许多机作业 Multiple Users
* 安全管理界面 – 设定管理用户权限（Roles) ，馆长、主任、馆员、用户等。
* 审计日志 Audit Log – 系统里，谁做了什么动作，都有记录。
* 收费管理（如果是学校、私人，这功能则没用）
* 以 telegram 续借、预约
* 使用 minio server  储存书和读者的照片

B) 系统采用的技术：

* Java 8
* Spring framework 5
* hibernate
* jooq
* thymeleaf
* mysql , mariadb (其实 hibernate 有支持的资料库都可以)
* bootstrap 4

这套系统的设计重点就是简单易用，普通人容易上手，解决微型图书馆的痛苦。
