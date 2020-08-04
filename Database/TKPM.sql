CREATE TABLE authors(
	author_id int(10) unsigned not null auto_increment,
	name nvarchar(50) not null,
	primary key(author_id)
);

CREATE TABLE categories(
	category_id int(10) unsigned not null auto_increment,
    name nvarchar(50) not null,
    primary key(category_id)
);

CREATE TABLE publishers(
	publisher_id int(10) unsigned not null auto_increment,
	name nvarchar(1000) not null,
    primary key(publisher_id)
);

CREATE TABLE books(
	book_id int(10) unsigned not null auto_increment,
    category_id int(10) unsigned not null,
    publisher_id int(10) unsigned not null,
    name nvarchar(50) not null,
    publisher nvarchar(50) not null,
    publicationYear int(10) not null,
    image nvarchar(1000) not null,
    primary key(book_id),
    foreign key(category_id) references categories(category_id),
    foreign key(publisher_id) references publishers(publisher_id)
);

CREATE TABLE amount_of_books(
	book_id int(10) unsigned not null,
    amount int(10) unsigned not null,
    foreign key(book_id) references books(book_id)
);

CREATE TABLE book_of_authors(
	book_id int(10) unsigned not null,
    author_id int(10) unsigned not null,
    foreign key(book_id) references books(book_id),
    foreign key(author_id) references authors(author_id)
);

CREATE TABLE roles(
	role_id enum('1', '2', '3') not null,
    name nvarchar(50) not null,
    primary key(role_id)
);

CREATE TABLE users(
	user_id int(10) unsigned unique not null,
    role_id enum('1', '2', '3') not null,
	name nvarchar(50) not null,
    gender enum('Nam', 'Nữ', 'Khác') not null,
    email nvarchar(50) not null,
    birthday date not null,
    address nvarchar(100) null,
    password nvarchar(100) not null,
    image nvarchar(1000) not null,
    primary key(user_id),
    foreign key(role_id) references roles(role_id)
);

CREATE TABLE bills(
	bill_id int(10) unsigned not null auto_increment,
    user_id int(10) unsigned not null,
    dateOfBorrowed date not null,
    dateOfPurchase date not null,
    primary key(bill_id),
    foreign key(user_id) references users(user_id)
);

CREATE TABLE bill_of_books(
	book_id int(10) unsigned not null,
    bill_id int(10) unsigned not null,
	foreign key(book_id) references books(book_id),
    foreign key(bill_id) references bills(bill_id)
);

CREATE TABLE rules(
	price int(10) unsigned not null,
    maxium_borrowed_books int(10) unsigned not null,
    maxium_borrowed_day int(10) unsigned not null
);