create database FYP;

create table SampleData (
	ID int not null auto_increment,
	Authors varchar(255),
	Title  varchar(255),
	Journal varchar(255),
	Proceeding varchar(255),
	Volume varchar(255),
	Issue varchar(255),
	Number varchar(255),
	Article varchar(255),
	Page varchar(255),
	Month varchar(255),
	Thesis varchar(255),
	Chapter varchar(255),
	BookTitle varchar(255),
	Editors varchar(255),
	Publisher varchar(255),
	Year int,
	PRIMARY KEY (ID)
);

select * from SampleData;

create table Journal (
	ID int not null auto_increment,
	FullName varchar(255),
	ShortName  varchar(255),
	PRIMARY KEY (ID)
);


select * from Journal;


create table Conference (
	ID int not null auto_increment,
	FullName varchar(255),
	ShortName  varchar(255),
	PRIMARY KEY (ID)
);


select * from Conference;