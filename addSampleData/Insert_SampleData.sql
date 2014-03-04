create database FYP;

create table Publication (
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
	Editors varchar(255),
	Publisher varchar(255),
	Year varchar(255),
	PRIMARY KEY (ID)
);

select * from Publication;

create table Journal (
	JID int not null auto_increment,
	FullName varchar(255),
	ShortName  varchar(255),
	PRIMARY KEY (JID)
);


select * from Journal;

create table Conference (
	CID int not null auto_increment,
	FullName varchar(255),
	ShortName  varchar(255),
	PRIMARY KEY (CID)
);


select * from Conference;