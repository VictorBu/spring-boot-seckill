create table if not exists user_info(
    id int not null auto_increment,
	name varchar(64) not null default '',
	gender tinyint not null default 0 comment '1: 男, 2: 女',
	age int not null default 0,
	telphone varchar(16) not null default '',
	register_mode varchar(64) not null default '' comment 'byphone, bywechat, byalipay',
	third_party_id varchar(64) not null default '',
	primary key (id),
    unique key index_user_info_telphone (telphone)
);

create table if not exists user_password (
	id int not null auto_increment,
	encrpt_password varchar(128) not null default '',
	user_id int not null default 0,
	primary key (id)
);

create table if not exists item (
    id int not null auto_increment,
    title varchar(64) not null default '',
    price double(10, 0) not null default 0,
    description varchar(500) null default '',
    sales int not null default 0,
    img_url varchar(200) null default '',
    primary key (id)
);

create table if not exists item_stock (
    id int not null auto_increment,
    stock int not null default 0,
    item_id int not null default 0,
    primary key (id)
);