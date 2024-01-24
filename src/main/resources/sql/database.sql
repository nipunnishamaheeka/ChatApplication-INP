drop
database if exists chatApp;
create
database if not exists chatApp;

use
chatApp;

create table login
(
    userName varchar(25) not null,
    password varchar(25) not null
);
