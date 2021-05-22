create table if not exists referenced_domain_object(
    id number primary key
);

create table if not exists domain_object(
      id number primary key
    , referenced_id number not null
    , data varchar2 not null check data not like ''
    , date date
    , foreign key(referenced_id) references referenced_domain_object(id)
);