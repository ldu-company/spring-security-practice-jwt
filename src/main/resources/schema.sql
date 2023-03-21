CREATE TABLE IF NOT EXISTS `USER`(
    id           bigint auto_increment primary key,
    username     varchar(255),
    password     varchar(255),
    authority    varchar(255)
);

insert into user
(id, username, password, authority)
VALUES
((select nvl(max(id)+1, 1) from user),
'user',
'{bcrypt}$2a$10$kWfGb/lt3wq4o15xnv3vVeVhGe1lFyCvRQpMiRnmEr0xlWacApJka',
'ROLE_USER'),
((select nvl(max(id)+1, 1) from user),
'admin',
'{bcrypt}$2a$10$u0ncA6t4F97risMq9vcGp.sv/gmbfMdnOGTYc7/CgCle9jiLVEGPi',
'ROLE_ADMIN');

CREATE TABLE IF NOT EXISTS `NOTICE`(
    id        bigint auto_increment primary key,
    title     varchar(255),
    content   varchar(255),
    created_At TIMESTAMP,
    updated_At TIMESTAMP
);

insert into notice
(ID , TITLE , CONTENT , CREATED_AT, UPDATED_AT )
VALUES
((select nvl(max(id)+1, 1) from notice), '환영합니다.', '환영합니다 여러분', sysdate, sysdate),
((select nvl(max(id)+1, 1) from notice),
'노트 작성 방법 공지.',
'1. 회원가입\n2. 로그인\n3. 노트 작성\n4. 저장\n* 본인 외에는 게시글을 볼 수 없습니다.', sysdate, sysdate)
;

CREATE TABLE IF NOT EXISTS `NOTE`(
    id         bigint auto_increment primary key,
    title      varchar(255),
    content    varchar(255),
    username   varchar(255),
    created_At TIMESTAMP,
    updated_At TIMESTAMP
);

insert into note
(ID, CONTENT, TITLE, USERNAME, CREATED_AT, UPDATED_AT )
values
((select nvl(max(id)+1,1) from note), '테스트', '테스트입니다', 'user', sysdate, sysdate),
((select nvl(max(id)+1,1) from note), '테스트2', '테스트2입니다', 'user', sysdate, sysdate),
((select nvl(max(id)+1,1) from note), '테스트3', '테스트3입니다', 'user', sysdate, sysdate),
((select nvl(max(id)+1,1) from note), '여름 여행계획', '여름 여행계획 작성중...', 'user', sysdate, sysdate);
