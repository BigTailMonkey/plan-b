CREATE TABLE `test_table`
(
    `id`             bigint(20)  NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL COMMENT '名称',
    PRIMARY KEY (`id`)
);

CREATE TABLE `test_table_2`
(
    `id`             bigint(20)  NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL COMMENT '名称',
    PRIMARY KEY (`id`)
);

CREATE TABLE `test_table_main`
(
    `id`             bigint(20)  NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL COMMENT '名称',
    PRIMARY KEY (`id`)
);