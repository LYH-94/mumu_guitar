CREATE DATABASE `mumu_guitar_db` CHARACTER SET 'utf8mb4';

USE mumu_guitar_db;

/* 根據 E-R 模型創建各實體的數據表。 */
/*===== 建表順序 1 =====*/
/* 用戶 - t_user */
CREATE TABLE IF NOT EXISTS `t_user` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`account` VARCHAR(50) UNIQUE NOT NULL,
	`password` VARCHAR(50) UNIQUE NOT NULL,
	`username` VARCHAR(20) NOT NULL,
	`gender` TINYINT UNSIGNED NOT NULL DEFAULT 1,
	`birthday` DATE DEFAULT NULL,
	`phone` VARCHAR(12) NOT NULL,
	`email` VARCHAR(30) NOT NULL,
	`identity` VARCHAR(10) NOT NULL DEFAULT 'general',
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

/* 新增 t_user 數據表的數據。 */
INSERT  INTO `t_user`(`id`,`account`,`password`,`username`,`gender`,`birthday`,`phone`,`email`,`identity`) VALUES
(0,'user001','pwd001','LYH',1,'1995-09-04','0912345678','user001@gmail.com','general'),
(0,'user002','pwd002','阿華',1,'1991-05-05','0987654321','user002@gmail.com','general'),
(0,'user003','pwd003','大大',1,'1989-11-21','0943215678','user003@gmail.com','general'),
(0,'user004','pwd004','小惠',0,'1999-01-10','0956784321','user004@gmail.com','general'),
(0,'user005','pwd005','阿武',1,'2003-07-09','0956781234','user005@gmail.com','general'),
(0,'root','abc123','管理員',1,'1234-12-12','0999999999','root@gmail.com','manager');

/*===== 建表順序 2 =====*/
/* 商品 - t_product */
CREATE TABLE IF NOT EXISTS `t_product` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`number` VARCHAR(10) UNIQUE NOT NULL,
	`photo` VARCHAR(10) NOT NULL,
	`name` VARCHAR(50) NOT NULL,
	`introduce` VARCHAR(200) NOT NULL,
	`brand` VARCHAR(10) NOT NULL,
	`model` VARCHAR(10) NOT NULL,
	`type` VARCHAR(10) NOT NULL,
	`inventory` MEDIUMINT UNSIGNED NOT NULL,
	`sales` MEDIUMINT UNSIGNED NOT NULL,
	`price` MEDIUMINT UNSIGNED NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

/* 新增 t_product 數據表的數據。 */
INSERT  INTO `t_product`(`id`,`number`,`photo`,`name`,`introduce`,`brand`,`model`,`type`,`inventory`,`sales`,`price`) VALUES 
(0,'2001','2001.jpg','電吉他A-粉','粉白配色的電吉他，擁有獨特迷人的外觀和卓越的音樂表現。這款吉他採用了優質的木材和精密的工藝，確保了出色的音質和舒適的演奏體驗。','A','ABC','電吉他',5,10,42900),
(0,'2002','2002.jpg','電吉他B-黑','BBB123','B','B200','電吉他',3,17,55200),
(0,'2003','2003.jpg','電吉他C-白','CCC123','C','C300','電吉他',5,20,39900),
(0,'2004','2004.jpg','電吉他D-白','DDD123','D','D400','電吉他',6,15,52900),
(0,'2005','2005.jpg','電吉他E-白','EEE123','E','E500','電吉他',2,23,22900),
(0,'2006','2006.jpg','電吉他F-黑','FFF123','F','F600','電吉他',4,10,45900),
(0,'2007','2007.jpg','電吉他G-黑','GGG123','G','G700','電吉他',4,13,63900);

/*===== 建表順序 3 =====*/
/* 訂單 - t_order */
CREATE TABLE IF NOT EXISTS `t_order` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`number` VARCHAR(20) UNIQUE NOT NULL,
	`date` DATETIME NOT NULL,
	`totalAmount` MEDIUMINT UNSIGNED NOT NULL,
	`status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
	`owner` INT NOT NULL,
	`purchaser` VARCHAR(20) NOT NULL,
	`phone` VARCHAR(12) NOT NULL,
	`payment` VARCHAR(20) NOT NULL DEFAULT '宅配；貨到付款',
	`address` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`),
	KEY `FK_host_user` (`owner`),
	CONSTRAINT `FK_order_user` FOREIGN KEY (`owner`) REFERENCES `t_user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

/* 新增 t_order 數據表的數據。 */
INSERT  INTO `t_order`(`id`,`number`,`date`,`totalAmount`,`status`,`owner`,`purchaser`,`phone`,`address`) VALUES 
(0,'202305040115233278',NOW(),161600,0,1,'林阿華','0912345678','XX市YY區ZZ街');

/*===== 建表順序 4 =====*/
/* 訂單 - t_orderProduct */
CREATE TABLE IF NOT EXISTS `t_orderProduct` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`product` INT NOT NULL,
	`quantity` MEDIUMINT UNSIGNED NOT NULL DEFAULT 1,
	`belongOrder` INT NOT NULL,
	PRIMARY KEY (`id`),
	KEY `FK_orderProduct_product` (`product`),
	CONSTRAINT `FK_orderProduct_product` FOREIGN KEY (`product`) REFERENCES `t_product` (`id`),
	KEY `FK_orderProduct_order` (`belongOrder`),
	CONSTRAINT `FK_orderProduct_order` FOREIGN KEY (`belongOrder`) REFERENCES `t_order` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

/* 新增 t_orderProduct 數據表的數據。 */
INSERT  INTO `t_orderProduct`(`id`,`product`,`quantity`,`belongOrder`) VALUES 
(0,1,2,1),
(0,4,1,1),
(0,5,1,1);

/*===== 建表順序 5 =====*/
/* 購物車項 - t_trolleyItem */
CREATE TABLE IF NOT EXISTS `t_trolleyItem` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`product` INT NOT NULL,
	`quantity` MEDIUMINT UNSIGNED NOT NULL DEFAULT 1,
	`owner` INT NOT NULL,
	PRIMARY KEY (`id`),
	KEY `FK_trolleyItem_product` (`product`),
	CONSTRAINT `FK_trolleyItem_product` FOREIGN KEY (`product`) REFERENCES `t_product` (`id`),
	KEY `FK_trolleyItem_user` (`owner`),
	CONSTRAINT `FK_trolleyItem_user` FOREIGN KEY (`owner`) REFERENCES `t_user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

/* 新增 t_trolleyItem 數據表的數據。 */
INSERT  INTO `t_trolleyItem`(`id`,`product`,`quantity`,`owner`) VALUES 
(0,3,1,1),
(0,2,1,3),
(0,3,1,3),
(0,5,1,3),
(0,1,1,4),
(0,5,1,4);

/*===== 建表順序 6 =====*/
/* 商品追蹤 - t_favorite */
CREATE TABLE IF NOT EXISTS `t_favorite` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`product` INT NOT NULL,
	`owner` INT NOT NULL,
	PRIMARY KEY (`id`),
	KEY `FK_favorite_product` (`product`),
	CONSTRAINT `FK_favorite_product` FOREIGN KEY (`product`) REFERENCES `t_product` (`id`),
	KEY `FK_favorite_user` (`owner`),
	CONSTRAINT `FK_favorite_user` FOREIGN KEY (`owner`) REFERENCES `t_user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

/* 新增 t_favorite 數據表的數據。 */
INSERT  INTO `t_favorite`(`id`,`product`,`owner`) VALUES 
(0,6,1),
(0,6,2),
(0,7,2),
(0,1,3),
(0,7,5);

/* ========== */