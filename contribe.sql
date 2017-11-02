CREATE DATABASE contribe;

use contribe;

CREATE TABLE shopping_item (
    shopping_item_id int not null auto_increment,
    price decimal(10,2) not null,
    item_type varchar(30) not null,
    title varchar(30),
    author varchar(30),
    primary key(shopping_item_id));

CREATE TABLE stock (
    stock_id int not null auto_increment,
    shopping_item_id int not null,
    quantity int not null,
    foreign key (shopping_item_id)
    references  shopping_item(shopping_item_id)
    on delete cascade,
    CONSTRAINT stock_unique UNIQUE (shopping_item_id),
    primary key(stock_id));
    
CREATE TABLE basket (
    basket_id int not null auto_increment,
    shopping_item_id int not null,
    foreign key (shopping_item_id)
    references  shopping_item(shopping_item_id)
    on delete cascade,
    primary key(basket_id));

CREATE USER 'contribe_root'@'localhost' IDENTIFIED BY 'contribe';

GRANT ALL PRIVILEGES ON contribe.* TO 'contribe_root'@'localhost' WITH GRANT OPTION;

