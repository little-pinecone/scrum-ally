
    create table project (
       id  bigserial not null,
        description varchar(255),
        name varchar(255),
        primary key (id)
    );

    create table project_member (
       id  bigserial not null,
        project_role varchar(255),
        project_id int8 not null,
        user_id int8,
        primary key (id)
    );

    create table task (
       id  bigserial not null,
        description varchar(255),
        name varchar(255),
        project_id int8 not null,
        primary key (id)
    );

    create table user_data (
       id  bigserial not null,
        password varchar(255),
        role varchar(255),
        username varchar(255),
        primary key (id)
    );

    alter table user_data 
       add constraint UK_nlc4atex50p892vsfhwccm336 unique (username);

    alter table project_member 
       add constraint FK103dwxad12nbaxtmnwus4eft2 
       foreign key (project_id) 
       references project 
       on delete cascade;

    alter table project_member 
       add constraint FKs78dyhytnqmuphtdlfko4pag3 
       foreign key (user_id) 
       references user_data 
       on delete cascade;

    alter table task 
       add constraint FKk8qrwowg31kx7hp93sru1pdqa 
       foreign key (project_id) 
       references project 
       on delete cascade;
