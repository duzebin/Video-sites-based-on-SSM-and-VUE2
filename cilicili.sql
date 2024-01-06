/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80028
Source Host           : localhost:3306
Source Database       : cilicili

Target Server Type    : MYSQL
Target Server Version : 80028
File Encoding         : 65001

Date: 2023-12-29 21:55:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `privileges` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_ibfk_1` (`privileges`),
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`privileges`) REFERENCES `admin_privileges` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('5', 'dzb', 'admin', '5f601be0468979fca8a2081cfa8339d4', '1');

-- ----------------------------
-- Table structure for admin_privileges
-- ----------------------------
DROP TABLE IF EXISTS `admin_privileges`;
CREATE TABLE `admin_privileges` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of admin_privileges
-- ----------------------------
INSERT INTO `admin_privileges` VALUES ('1', '超级用户');
INSERT INTO `admin_privileges` VALUES ('2', '视频管理员');
INSERT INTO `admin_privileges` VALUES ('3', '评论管理员');
INSERT INTO `admin_privileges` VALUES ('4', '用户管理员');

-- ----------------------------
-- Table structure for audit
-- ----------------------------
DROP TABLE IF EXISTS `audit`;
CREATE TABLE `audit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of audit
-- ----------------------------
INSERT INTO `audit` VALUES ('1', '正常');
INSERT INTO `audit` VALUES ('2', '审核中');
INSERT INTO `audit` VALUES ('3', '未通过');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vid` int NOT NULL,
  `uid` int NOT NULL,
  `text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `adhere` int DEFAULT NULL,
  `like` int NOT NULL,
  `uptime` datetime NOT NULL,
  `state` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `comments_ibfk_3` (`uid`),
  KEY `comments_ibfk_4` (`adhere`),
  KEY `comments_ibfk_5` (`state`),
  KEY `comments_ibfk_2` (`vid`),
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`vid`) REFERENCES `video` (`vid`),
  CONSTRAINT `comments_ibfk_3` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comments_ibfk_4` FOREIGN KEY (`adhere`) REFERENCES `comments` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comments_ibfk_5` FOREIGN KEY (`state`) REFERENCES `audit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=369 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES ('317', '62', '42', '学到了', null, '2', '2023-12-07 15:37:19', '1');
INSERT INTO `comments` VALUES ('319', '62', '44', '111', '317', '1', '2023-12-07 16:00:45', '1');
INSERT INTO `comments` VALUES ('320', '62', '44', '222', '317', '1', '2023-12-07 16:00:48', '1');
INSERT INTO `comments` VALUES ('321', '62', '44', '333', '317', '0', '2023-12-07 16:00:50', '1');
INSERT INTO `comments` VALUES ('322', '62', '44', '444', '317', '0', '2023-12-07 16:00:52', '1');
INSERT INTO `comments` VALUES ('323', '62', '44', '1111', null, '0', '2023-12-07 16:00:54', '1');
INSERT INTO `comments` VALUES ('324', '62', '44', '2222', null, '0', '2023-12-07 16:00:55', '1');
INSERT INTO `comments` VALUES ('325', '62', '44', '4444', null, '0', '2023-12-07 16:00:57', '1');
INSERT INTO `comments` VALUES ('326', '62', '44', '5555', null, '0', '2023-12-07 16:00:58', '1');
INSERT INTO `comments` VALUES ('327', '62', '44', '6666', null, '0', '2023-12-07 16:00:59', '1');
INSERT INTO `comments` VALUES ('328', '62', '44', '7777', null, '0', '2023-12-07 16:01:02', '1');
INSERT INTO `comments` VALUES ('329', '62', '44', '8888', null, '0', '2023-12-07 16:01:03', '1');
INSERT INTO `comments` VALUES ('330', '62', '44', '9999', null, '0', '2023-12-07 16:01:04', '1');
INSERT INTO `comments` VALUES ('331', '62', '44', '0000', null, '0', '2023-12-07 16:01:06', '1');
INSERT INTO `comments` VALUES ('332', '62', '44', '1111', null, '0', '2023-12-07 16:01:08', '1');
INSERT INTO `comments` VALUES ('333', '62', '44', '0', '317', '0', '2023-12-07 16:01:10', '1');
INSERT INTO `comments` VALUES ('334', '62', '44', '1', '317', '0', '2023-12-07 16:01:10', '1');
INSERT INTO `comments` VALUES ('335', '62', '44', '23', '317', '0', '2023-12-07 16:01:12', '1');
INSERT INTO `comments` VALUES ('336', '62', '44', '3', '317', '0', '2023-12-07 16:01:13', '1');
INSERT INTO `comments` VALUES ('337', '62', '44', '4', '317', '0', '2023-12-07 16:01:14', '1');
INSERT INTO `comments` VALUES ('338', '62', '44', '5', '317', '0', '2023-12-07 16:01:15', '1');
INSERT INTO `comments` VALUES ('339', '62', '44', '6', '317', '0', '2023-12-07 16:01:16', '1');
INSERT INTO `comments` VALUES ('340', '62', '44', '1', null, '0', '2023-12-07 16:02:02', '1');
INSERT INTO `comments` VALUES ('341', '62', '44', '1111111', null, '0', '2023-12-07 16:02:27', '1');
INSERT INTO `comments` VALUES ('342', '62', '44', '0000', null, '0', '2023-12-07 16:02:50', '1');
INSERT INTO `comments` VALUES ('343', '62', '44', '啊啊啊啊', null, '0', '2023-12-07 16:02:58', '1');
INSERT INTO `comments` VALUES ('344', '65', '41', '好好好', null, '0', '2023-12-09 18:46:27', '1');
INSERT INTO `comments` VALUES ('345', '65', '41', '好好好', null, '0', '2023-12-09 18:46:31', '1');
INSERT INTO `comments` VALUES ('351', '66', '41', '111111', null, '1', '2023-12-09 19:38:45', '1');
INSERT INTO `comments` VALUES ('352', '66', '41', '222222', null, '1', '2023-12-09 19:38:47', '1');
INSERT INTO `comments` VALUES ('353', '66', '41', '333333', null, '1', '2023-12-09 19:38:49', '1');
INSERT INTO `comments` VALUES ('354', '66', '41', '444444', null, '0', '2023-12-09 19:38:51', '1');
INSERT INTO `comments` VALUES ('355', '66', '41', '555555', null, '0', '2023-12-09 19:38:52', '1');
INSERT INTO `comments` VALUES ('356', '66', '41', '666666', null, '0', '2023-12-09 19:38:54', '1');
INSERT INTO `comments` VALUES ('357', '66', '41', '777777', null, '0', '2023-12-09 19:38:56', '1');
INSERT INTO `comments` VALUES ('358', '66', '41', '888888', null, '0', '2023-12-09 19:38:58', '1');
INSERT INTO `comments` VALUES ('359', '66', '41', '999999', null, '0', '2023-12-09 19:38:59', '1');
INSERT INTO `comments` VALUES ('360', '66', '41', '000000', null, '0', '2023-12-09 19:39:01', '1');
INSERT INTO `comments` VALUES ('361', '66', '41', '111111', null, '0', '2023-12-09 19:39:04', '1');
INSERT INTO `comments` VALUES ('362', '66', '41', '111111', '351', '1', '2023-12-09 19:39:31', '1');
INSERT INTO `comments` VALUES ('363', '66', '41', '111111', '351', '0', '2023-12-09 19:39:33', '1');
INSERT INTO `comments` VALUES ('364', '66', '41', '111111', '351', '0', '2023-12-09 19:39:34', '1');
INSERT INTO `comments` VALUES ('365', '66', '41', '11111', '351', '0', '2023-12-09 19:39:36', '1');
INSERT INTO `comments` VALUES ('366', '66', '41', '111111', '351', '0', '2023-12-09 19:39:37', '1');
INSERT INTO `comments` VALUES ('367', '66', '41', '1111', '351', '0', '2023-12-09 19:48:08', '1');
INSERT INTO `comments` VALUES ('368', '62', '41', '1111', null, '0', '2023-12-21 12:13:11', '2');

-- ----------------------------
-- Table structure for comments_like
-- ----------------------------
DROP TABLE IF EXISTS `comments_like`;
CREATE TABLE `comments_like` (
  `uid` int NOT NULL,
  `cid` int NOT NULL,
  PRIMARY KEY (`uid`,`cid`),
  KEY `comments_like_ibfk_2` (`cid`),
  CONSTRAINT `comments_like_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comments_like_ibfk_2` FOREIGN KEY (`cid`) REFERENCES `comments` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of comments_like
-- ----------------------------
INSERT INTO `comments_like` VALUES ('41', '317');
INSERT INTO `comments_like` VALUES ('44', '317');
INSERT INTO `comments_like` VALUES ('44', '319');
INSERT INTO `comments_like` VALUES ('44', '320');
INSERT INTO `comments_like` VALUES ('41', '351');
INSERT INTO `comments_like` VALUES ('41', '352');
INSERT INTO `comments_like` VALUES ('41', '353');
INSERT INTO `comments_like` VALUES ('41', '362');

-- ----------------------------
-- Table structure for concerns
-- ----------------------------
DROP TABLE IF EXISTS `concerns`;
CREATE TABLE `concerns` (
  `uid` int NOT NULL,
  `fans` int NOT NULL,
  PRIMARY KEY (`uid`,`fans`),
  KEY `fans` (`fans`),
  CONSTRAINT `concerns_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `concerns_ibfk_2` FOREIGN KEY (`fans`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of concerns
-- ----------------------------
INSERT INTO `concerns` VALUES ('42', '41');
INSERT INTO `concerns` VALUES ('43', '41');
INSERT INTO `concerns` VALUES ('41', '42');
INSERT INTO `concerns` VALUES ('41', '43');
INSERT INTO `concerns` VALUES ('42', '43');
INSERT INTO `concerns` VALUES ('41', '44');
INSERT INTO `concerns` VALUES ('42', '44');
INSERT INTO `concerns` VALUES ('43', '44');

-- ----------------------------
-- Table structure for episode
-- ----------------------------
DROP TABLE IF EXISTS `episode`;
CREATE TABLE `episode` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL COMMENT '分集人ID',
  `ename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分集名',
  `crtime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `episode_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of episode
-- ----------------------------
INSERT INTO `episode` VALUES ('10', '41', '鬼畜星探S13合集', '2023-12-07 14:53:45');
INSERT INTO `episode` VALUES ('11', '42', '【DJ摩尔加纳】P5R音乐排行榜', '2023-12-07 15:23:10');
INSERT INTO `episode` VALUES ('12', '43', '我的妹妹不可能那么可爱第一季', '2023-12-07 15:48:47');

-- ----------------------------
-- Table structure for file_chunk
-- ----------------------------
DROP TABLE IF EXISTS `file_chunk`;
CREATE TABLE `file_chunk` (
  `md5` varchar(255) NOT NULL,
  `num` int NOT NULL,
  `count` int NOT NULL,
  PRIMARY KEY (`md5`,`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of file_chunk
-- ----------------------------

-- ----------------------------
-- Table structure for level
-- ----------------------------
DROP TABLE IF EXISTS `level`;
CREATE TABLE `level` (
  `level` int NOT NULL AUTO_INCREMENT COMMENT '等级',
  `experience` int NOT NULL COMMENT '升级所需经验值',
  PRIMARY KEY (`level`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of level
-- ----------------------------
INSERT INTO `level` VALUES ('1', '0');
INSERT INTO `level` VALUES ('2', '256');
INSERT INTO `level` VALUES ('3', '1024');
INSERT INTO `level` VALUES ('4', '4096');
INSERT INTO `level` VALUES ('5', '16384');
INSERT INTO `level` VALUES ('6', '32768');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `aid` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `msg` varchar(255) NOT NULL,
  `uptime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `aid` (`aid`),
  CONSTRAINT `notice_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES ('2', '5', '11111', '11111', '2023-12-06 01:24:21');
INSERT INTO `notice` VALUES ('3', '5', 'vsdvsd', 'vdavasd', '2023-12-06 01:54:31');
INSERT INTO `notice` VALUES ('4', '5', '111111111171717', '1111111111171771', '2023-12-06 15:14:34');
INSERT INTO `notice` VALUES ('5', '5', '1718752875328', '78527543278526378542', '2023-12-06 15:18:43');
INSERT INTO `notice` VALUES ('6', '5', '11111111111111111111111111111111111111111', '1111111111111111111111111111111111111111111111111', '2023-12-06 15:22:20');
INSERT INTO `notice` VALUES ('7', '5', '1111111111111111111111111111111111111111111111', '111111111111111111111111111111111111111111111111111111', '2023-12-06 15:22:29');
INSERT INTO `notice` VALUES ('10', '5', '123456', '123456', '2023-12-06 17:39:20');

-- ----------------------------
-- Table structure for partition
-- ----------------------------
DROP TABLE IF EXISTS `partition`;
CREATE TABLE `partition` (
  `pid` int NOT NULL AUTO_INCREMENT COMMENT '分区id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分区名',
  PRIMARY KEY (`pid`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of partition
-- ----------------------------
INSERT INTO `partition` VALUES ('45', '动物');
INSERT INTO `partition` VALUES ('27', '动画');
INSERT INTO `partition` VALUES ('25', '国创');
INSERT INTO `partition` VALUES ('30', '娱乐');
INSERT INTO `partition` VALUES ('40', '影视');
INSERT INTO `partition` VALUES ('46', '搞笑');
INSERT INTO `partition` VALUES ('44', '时尚');
INSERT INTO `partition` VALUES ('33', '汽车');
INSERT INTO `partition` VALUES ('38', '游戏');
INSERT INTO `partition` VALUES ('43', '生活');
INSERT INTO `partition` VALUES ('35', '电影');
INSERT INTO `partition` VALUES ('36', '电视');
INSERT INTO `partition` VALUES ('24', '番剧');
INSERT INTO `partition` VALUES ('41', '知识');
INSERT INTO `partition` VALUES ('31', '科技');
INSERT INTO `partition` VALUES ('37', '纪录');
INSERT INTO `partition` VALUES ('26', '综艺');
INSERT INTO `partition` VALUES ('32', '美食');
INSERT INTO `partition` VALUES ('29', '舞蹈');
INSERT INTO `partition` VALUES ('42', '资讯');
INSERT INTO `partition` VALUES ('34', '运动');
INSERT INTO `partition` VALUES ('39', '音乐');
INSERT INTO `partition` VALUES ('28', '鬼畜');

-- ----------------------------
-- Table structure for publicize
-- ----------------------------
DROP TABLE IF EXISTS `publicize`;
CREATE TABLE `publicize` (
  `id` int NOT NULL AUTO_INCREMENT,
  `img` varchar(255) NOT NULL,
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `type` (`type`),
  CONSTRAINT `publicize_ibfk_1` FOREIGN KEY (`type`) REFERENCES `publicize_type` (`name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of publicize
-- ----------------------------
INSERT INTO `publicize` VALUES ('1', '/publicize/01.png', '首页轮播');
INSERT INTO `publicize` VALUES ('2', '/publicize/02.png', '首页轮播');
INSERT INTO `publicize` VALUES ('3', '/publicize/03.png', '首页轮播');
INSERT INTO `publicize` VALUES ('4', '/publicize/04.png', '首页轮播');
INSERT INTO `publicize` VALUES ('5', '/publicize/001.jpg', '投稿活动轮播');
INSERT INTO `publicize` VALUES ('6', '/publicize/002.jpg', '投稿活动轮播');
INSERT INTO `publicize` VALUES ('7', '/publicize/003.jpg', '投稿活动轮播');
INSERT INTO `publicize` VALUES ('8', '/publicize/004.jpg', '投稿活动轮播');
INSERT INTO `publicize` VALUES ('9', '/publicize/005.jpg', '投稿活动轮播');

-- ----------------------------
-- Table structure for publicize_type
-- ----------------------------
DROP TABLE IF EXISTS `publicize_type`;
CREATE TABLE `publicize_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '轮播图类型名',
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of publicize_type
-- ----------------------------
INSERT INTO `publicize_type` VALUES ('2', '投稿活动轮播');
INSERT INTO `publicize_type` VALUES ('1', '首页轮播');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES ('1', 'nsfuabegjkbajkedg');
INSERT INTO `tag` VALUES ('2', '这是一个标签');
INSERT INTO `tag` VALUES ('26', '123456');
INSERT INTO `tag` VALUES ('27', '123');
INSERT INTO `tag` VALUES ('28', '456');
INSERT INTO `tag` VALUES ('29', '1234577');
INSERT INTO `tag` VALUES ('30', '1111');
INSERT INTO `tag` VALUES ('31', '147');
INSERT INTO `tag` VALUES ('32', '789');
INSERT INTO `tag` VALUES ('33', '7898');
INSERT INTO `tag` VALUES ('34', '4546');
INSERT INTO `tag` VALUES ('35', '4252542');
INSERT INTO `tag` VALUES ('36', '1425');
INSERT INTO `tag` VALUES ('37', '122456');
INSERT INTO `tag` VALUES ('38', '3543453');
INSERT INTO `tag` VALUES ('39', '3785375');
INSERT INTO `tag` VALUES ('40', '3754');
INSERT INTO `tag` VALUES ('41', '富爸爸好');
INSERT INTO `tag` VALUES ('42', '453853');
INSERT INTO `tag` VALUES ('43', '方式不得分');
INSERT INTO `tag` VALUES ('44', '455');
INSERT INTO `tag` VALUES ('45', '12345');
INSERT INTO `tag` VALUES ('46', '25/8');
INSERT INTO `tag` VALUES ('47', '嘚瑟发给');
INSERT INTO `tag` VALUES ('48', '111111');
INSERT INTO `tag` VALUES ('49', '4444');
INSERT INTO `tag` VALUES ('50', '知识');
INSERT INTO `tag` VALUES ('51', '游戏');
INSERT INTO `tag` VALUES ('52', '哆啦a梦');
INSERT INTO `tag` VALUES ('53', '111');
INSERT INTO `tag` VALUES ('54', '222');
INSERT INTO `tag` VALUES ('55', '333');
INSERT INTO `tag` VALUES ('56', 'p5');
INSERT INTO `tag` VALUES ('57', '女神异闻录5');
INSERT INTO `tag` VALUES ('58', '财');
INSERT INTO `tag` VALUES ('59', '鬼畜');
INSERT INTO `tag` VALUES ('60', '活着');
INSERT INTO `tag` VALUES ('61', '11111');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `lastLogin` datetime NOT NULL,
  `state` int NOT NULL COMMENT '用户状态',
  PRIMARY KEY (`uid`),
  KEY `state` (`state`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`state`) REFERENCES `user_state` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('41', '111111', 'c92014c627c2cef220e8ee099f1eafe9', '2023-12-21 12:12:58', '1');
INSERT INTO `user` VALUES ('42', '222222', 'e12427de6329bf56f158bd6a425f1382', '2023-12-09 19:41:38', '1');
INSERT INTO `user` VALUES ('43', '333333', 'a17a6e93e1288b516563f8643a5e7bb1', '2023-12-09 18:32:26', '1');
INSERT INTO `user` VALUES ('44', '444444', 'b45522faaac1bf398f72627c9960aec2', '2023-12-09 18:32:34', '1');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `uid` int NOT NULL COMMENT '用户id',
  `uname` varchar(255) NOT NULL COMMENT '用户名',
  `avatar` varchar(255) NOT NULL COMMENT '头像',
  `experience` int NOT NULL COMMENT '经验值',
  `level` int NOT NULL COMMENT '当前等级',
  `describes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '个人介绍',
  `vip` tinyint(1) NOT NULL COMMENT '是否vip',
  `vipEndTime` datetime DEFAULT NULL,
  `concerns` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关注数',
  `fans` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '粉丝数',
  PRIMARY KEY (`uid`),
  KEY `level` (`level`),
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_info_ibfk_2` FOREIGN KEY (`level`) REFERENCES `level` (`level`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('41', 'dzb', '/avatar/41_1701871846908.jpg', '70', '1', '12356777', '0', null, '2', '3');
INSERT INTO `user_info` VALUES ('42', '222222', '/avatar/42.jpg', '40', '1', '222222', '0', null, '1', '3');
INSERT INTO `user_info` VALUES ('43', '333333', '/avatar/43.jpg', '20', '1', '333333', '0', null, '2', '2');
INSERT INTO `user_info` VALUES ('44', '444444', '/avatar/44.jpg', '20', '1', '444444', '0', null, '3', '0');

-- ----------------------------
-- Table structure for user_state
-- ----------------------------
DROP TABLE IF EXISTS `user_state`;
CREATE TABLE `user_state` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态名',
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of user_state
-- ----------------------------
INSERT INTO `user_state` VALUES ('1', '正常');
INSERT INTO `user_state` VALUES ('2', '禁止登录');
INSERT INTO `user_state` VALUES ('3', '禁止评论');

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `vid` int NOT NULL AUTO_INCREMENT COMMENT '视频id',
  `uid` int DEFAULT NULL COMMENT '上传人id',
  `title` varchar(255) NOT NULL COMMENT '视频标题',
  `synopsis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '视频简介',
  `cover` varchar(255) NOT NULL COMMENT '视频封面',
  `src` varchar(255) NOT NULL COMMENT '视频链接',
  `play` int NOT NULL COMMENT '播放量',
  `like` int NOT NULL COMMENT '点赞量',
  `favorites` int NOT NULL COMMENT '收藏量',
  `uptime` datetime NOT NULL COMMENT '上传时间',
  `partition` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '隶属分区',
  `episode` int DEFAULT NULL COMMENT '隶属分集',
  `state` int NOT NULL COMMENT '视频状态',
  PRIMARY KEY (`vid`),
  KEY `uid` (`uid`),
  KEY `episode` (`episode`),
  KEY `video_ibfk_2` (`partition`),
  KEY `vstate` (`state`) USING BTREE,
  CONSTRAINT `video_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_ibfk_2` FOREIGN KEY (`partition`) REFERENCES `partition` (`name`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_ibfk_3` FOREIGN KEY (`episode`) REFERENCES `episode` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_ibfk_4` FOREIGN KEY (`state`) REFERENCES `audit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES ('60', '41', '卡拉彼丘   2023-08-05 03-02-26', '游戏', '/video/cover/41_2ccb0d301cbdfe831ae5040ab4fd2ea5.jpg', '/video/41_2ccb0d301cbdfe831ae5040ab4fd2ea5.mp4', '0', '0', '0', '2023-12-06 23:21:50', '游戏', null, '3');
INSERT INTO `video` VALUES ('62', '41', '41_0632762ec21917931e6a9bbe85b67aa0', '123456', '/video/cover/41_0632762ec21917931e6a9bbe85b67aa0.jpg', '/video/41_0632762ec21917931e6a9bbe85b67aa0.mp4', '0', '3', '3', '2023-12-07 13:21:18', '知识', null, '1');
INSERT INTO `video` VALUES ('65', '41', '财，就多敛', '不义之财！绝不能收！\r\n交给我处理', '/video/cover/41_e8579f46159b9a18293ab8b846a02445.jpg', '/video/41_e8579f46159b9a18293ab8b846a02445.mp4', '0', '1', '2', '2023-12-07 14:54:20', '鬼畜', '10', '1');
INSERT INTO `video` VALUES ('66', '42', '第1位《Life Will Change》', '先前曾在日推上征集粉丝票选《女神异闻录5 皇家版》的推荐乐曲，在此DJ摩尔加纳将以排行榜的形式为大家带来票选的结果~', '/video/cover/42_48c5c2a1a5e8225c7ffed69edb937302.jpg', '/video/42_48c5c2a1a5e8225c7ffed69edb937302.mp4', '0', '3', '3', '2023-12-07 15:25:32', '音乐', '11', '1');
INSERT INTO `video` VALUES ('67', '42', '第2位《I believe》', '先前曾在日推上征集粉丝票选《女神异闻录5 皇家版》的推荐乐曲，在此DJ摩尔加纳将以排行榜的形式为大家带来票选的结果~', '/video/cover/42_1701934346041.jpg', '/video/42_8d3c1f27d8757fc3cdb028630c00c8fd.mp4', '0', '1', '1', '2023-12-07 15:26:57', '音乐', '11', '1');
INSERT INTO `video` VALUES ('68', '42', '第3位《Rivers In the Desert》', null, '/video/cover/42_b9e7a36ef56c433df9270807854e6ec0.jpg', '/video/42_b9e7a36ef56c433df9270807854e6ec0.mp4', '0', '1', '1', '2023-12-07 15:29:09', '音乐', '11', '1');
INSERT INTO `video` VALUES ('69', '42', '第7位《Wake Up, Get Up, Get Out There》', 'p5', '/video/cover/42_1701934456111.jpg', '/video/42_e1e706ee341ae06f354abb6c3c382ccc.mp4', '0', '1', '1', '2023-12-07 15:29:42', '音乐', '11', '1');
INSERT INTO `video` VALUES ('70', '43', '张艺谋最好的电影，葛优凭此片获封影帝！改编自余华《活着》', '活着', '/video/cover/43_e858168ffec01efb9cf133d95f6fe2d8.jpg', '/video/43_e858168ffec01efb9cf133d95f6fe2d8.mp4', '0', '1', '1', '2023-12-07 15:44:48', '电影', null, '1');
INSERT INTO `video` VALUES ('71', '43', '第15话 我的妹妹哪有这样就最终回', '11111', '/video/cover/43_de58b70d034ae1187ad3f4a259e2e2c4.jpg', '/video/43_de58b70d034ae1187ad3f4a259e2e2c4.mp4', '0', '1', '1', '2023-12-07 15:49:15', '番剧', '12', '1');

-- ----------------------------
-- Table structure for video_favorites
-- ----------------------------
DROP TABLE IF EXISTS `video_favorites`;
CREATE TABLE `video_favorites` (
  `uid` int NOT NULL,
  `vid` int NOT NULL,
  PRIMARY KEY (`uid`,`vid`),
  KEY `favorites_ibfk_2` (`vid`),
  CONSTRAINT `video_favorites_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_favorites_ibfk_2` FOREIGN KEY (`vid`) REFERENCES `video` (`vid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of video_favorites
-- ----------------------------
INSERT INTO `video_favorites` VALUES ('41', '62');
INSERT INTO `video_favorites` VALUES ('42', '62');
INSERT INTO `video_favorites` VALUES ('44', '62');
INSERT INTO `video_favorites` VALUES ('41', '65');
INSERT INTO `video_favorites` VALUES ('44', '65');
INSERT INTO `video_favorites` VALUES ('41', '66');
INSERT INTO `video_favorites` VALUES ('42', '66');
INSERT INTO `video_favorites` VALUES ('44', '66');
INSERT INTO `video_favorites` VALUES ('44', '67');
INSERT INTO `video_favorites` VALUES ('44', '68');
INSERT INTO `video_favorites` VALUES ('44', '69');
INSERT INTO `video_favorites` VALUES ('44', '70');
INSERT INTO `video_favorites` VALUES ('44', '71');

-- ----------------------------
-- Table structure for video_files
-- ----------------------------
DROP TABLE IF EXISTS `video_files`;
CREATE TABLE `video_files` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `md5` varchar(255) NOT NULL,
  `count` int NOT NULL,
  `state` int NOT NULL,
  `vid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `vid` (`vid`),
  CONSTRAINT `video_files_ibfk_1` FOREIGN KEY (`vid`) REFERENCES `video` (`vid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of video_files
-- ----------------------------
INSERT INTO `video_files` VALUES ('112', '41', '2ccb0d301cbdfe831ae5040ab4fd2ea5', '27', '1', '60');
INSERT INTO `video_files` VALUES ('115', '41', '0632762ec21917931e6a9bbe85b67aa0', '2', '1', '62');
INSERT INTO `video_files` VALUES ('118', '41', 'e8579f46159b9a18293ab8b846a02445', '2', '1', '65');
INSERT INTO `video_files` VALUES ('119', '42', '48c5c2a1a5e8225c7ffed69edb937302', '10', '1', '66');
INSERT INTO `video_files` VALUES ('120', '42', '8d3c1f27d8757fc3cdb028630c00c8fd', '8', '1', '67');
INSERT INTO `video_files` VALUES ('121', '42', 'b9e7a36ef56c433df9270807854e6ec0', '7', '1', '68');
INSERT INTO `video_files` VALUES ('122', '42', 'e1e706ee341ae06f354abb6c3c382ccc', '6', '1', '69');
INSERT INTO `video_files` VALUES ('123', '43', 'e858168ffec01efb9cf133d95f6fe2d8', '49', '1', '70');
INSERT INTO `video_files` VALUES ('124', '43', 'de58b70d034ae1187ad3f4a259e2e2c4', '45', '1', '71');

-- ----------------------------
-- Table structure for video_like
-- ----------------------------
DROP TABLE IF EXISTS `video_like`;
CREATE TABLE `video_like` (
  `uid` int NOT NULL,
  `vid` int NOT NULL,
  PRIMARY KEY (`uid`,`vid`),
  KEY `video_like_ibfk_2` (`vid`),
  CONSTRAINT `video_like_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_like_ibfk_2` FOREIGN KEY (`vid`) REFERENCES `video` (`vid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of video_like
-- ----------------------------
INSERT INTO `video_like` VALUES ('41', '62');
INSERT INTO `video_like` VALUES ('42', '62');
INSERT INTO `video_like` VALUES ('44', '62');
INSERT INTO `video_like` VALUES ('44', '65');
INSERT INTO `video_like` VALUES ('41', '66');
INSERT INTO `video_like` VALUES ('42', '66');
INSERT INTO `video_like` VALUES ('44', '66');
INSERT INTO `video_like` VALUES ('44', '67');
INSERT INTO `video_like` VALUES ('44', '68');
INSERT INTO `video_like` VALUES ('44', '69');
INSERT INTO `video_like` VALUES ('44', '70');
INSERT INTO `video_like` VALUES ('44', '71');

-- ----------------------------
-- Table structure for video_message
-- ----------------------------
DROP TABLE IF EXISTS `video_message`;
CREATE TABLE `video_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `aid` int NOT NULL,
  `uid` int NOT NULL,
  `vid` int NOT NULL,
  `msg` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  `state` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `vid` (`vid`),
  KEY `type` (`state`),
  KEY `aid` (`aid`),
  CONSTRAINT `video_message_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_message_ibfk_2` FOREIGN KEY (`vid`) REFERENCES `video` (`vid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_message_ibfk_3` FOREIGN KEY (`state`) REFERENCES `audit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_message_ibfk_4` FOREIGN KEY (`aid`) REFERENCES `admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of video_message
-- ----------------------------
INSERT INTO `video_message` VALUES ('8', '5', '41', '60', '视频封面未通过审核', '2023-12-06 23:22:38', '3');
INSERT INTO `video_message` VALUES ('9', '5', '41', '60', '通过审核', '2023-12-06 23:49:51', '1');
INSERT INTO `video_message` VALUES ('10', '5', '41', '60', '视频简介未通过审核', '2023-12-06 23:51:00', '3');
INSERT INTO `video_message` VALUES ('11', '5', '41', '60', '通过审核', '2023-12-06 23:52:03', '1');
INSERT INTO `video_message` VALUES ('12', '5', '41', '60', '视频标题未通过审核', '2023-12-06 23:53:09', '3');
INSERT INTO `video_message` VALUES ('14', '5', '41', '62', '通过审核', '2023-12-07 13:51:58', '1');
INSERT INTO `video_message` VALUES ('17', '5', '41', '65', '通过审核', '2023-12-07 14:54:35', '1');
INSERT INTO `video_message` VALUES ('18', '5', '42', '66', '通过审核', '2023-12-07 15:25:47', '1');
INSERT INTO `video_message` VALUES ('19', '5', '42', '67', '通过审核', '2023-12-07 15:27:08', '1');
INSERT INTO `video_message` VALUES ('20', '5', '42', '69', '通过审核', '2023-12-07 15:30:04', '1');
INSERT INTO `video_message` VALUES ('21', '5', '42', '68', '通过审核', '2023-12-07 15:30:06', '1');
INSERT INTO `video_message` VALUES ('22', '5', '43', '70', '通过审核', '2023-12-07 15:45:11', '1');
INSERT INTO `video_message` VALUES ('23', '5', '43', '71', '通过审核', '2023-12-07 15:51:14', '1');
INSERT INTO `video_message` VALUES ('24', '5', '42', '66', '视频抽检标题未通过审核', '2023-12-07 19:42:57', '3');
INSERT INTO `video_message` VALUES ('25', '5', '42', '66', '通过审核', '2023-12-07 19:50:20', '1');
INSERT INTO `video_message` VALUES ('26', '5', '42', '66', '通过审核', '2023-12-07 19:56:13', '1');
INSERT INTO `video_message` VALUES ('27', '5', '42', '67', '通过审核', '2023-12-07 19:56:49', '1');
INSERT INTO `video_message` VALUES ('30', '5', '42', '66', '通过审核', '2023-12-09 19:42:17', '1');

-- ----------------------------
-- Table structure for video_tag
-- ----------------------------
DROP TABLE IF EXISTS `video_tag`;
CREATE TABLE `video_tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vid` int NOT NULL,
  `tid` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vid` (`vid`),
  KEY `video_tag_ibfk_2` (`tid`),
  CONSTRAINT `video_tag_ibfk_1` FOREIGN KEY (`vid`) REFERENCES `video` (`vid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `video_tag_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `tag` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of video_tag
-- ----------------------------
INSERT INTO `video_tag` VALUES ('103', '60', '51');
INSERT INTO `video_tag` VALUES ('105', '62', '53');
INSERT INTO `video_tag` VALUES ('106', '62', '54');
INSERT INTO `video_tag` VALUES ('107', '62', '55');
INSERT INTO `video_tag` VALUES ('111', '65', '58');
INSERT INTO `video_tag` VALUES ('112', '65', '59');
INSERT INTO `video_tag` VALUES ('113', '66', '56');
INSERT INTO `video_tag` VALUES ('114', '66', '57');
INSERT INTO `video_tag` VALUES ('115', '67', '56');
INSERT INTO `video_tag` VALUES ('116', '68', '56');
INSERT INTO `video_tag` VALUES ('117', '69', '56');
INSERT INTO `video_tag` VALUES ('118', '70', '60');
INSERT INTO `video_tag` VALUES ('119', '71', '61');
