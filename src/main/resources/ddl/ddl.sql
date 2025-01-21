################################ 유저 관련 테이블 ################################
create table users (
    user_id     bigint          primary key not null,
    login_type  enum('LOCAL', 'GITHUB', 'KAKAO', 'NAVER', 'GOOGLE') not null default 'LOCAL',
    login_id    varchar(255)    not null unique,
    login_pw    varchar(255)    not null,
    nickname    varchar(255)    null unique,
    profile_img varchar(255)    null,
    status      enum('ACTIVE', 'PAUSE', 'BAN') not null default 'ACTIVE',
    score       int(11)         not null,
    init_score  int(11)         not null,
    role        tinyint(1)      not null default 0,
    created_dt  datetime(6)     null default current_timestamp(6),
    updated_dt  datetime(6)     null default current_timestamp(6) on update current_timestamp(6),
    removed_dt  datetime(6)     null
);

################################ 문제 관련 테이블 ################################
create table questions (
    question_id     bigint      primary key not null,
    question        longtext    not null,
    category        enum('ALGORITHUM_MAQ', 'ALGORITHUM_UAQ', 'ALGORITHUM_SAQ', 'OS_MAQ', 'OS_UAQ', 'OS_SAQ', 'NETWORK_MAQ', 'NETWORK_UAQ', 'NETWORK_SAQ', 'DB_MAQ', 'DB_UAQ', 'DB_SAQ', 'DESIGN_MAQ', 'DESIGN_UAQ', 'DESIGN_SAQ') not null,
    comment         longtext    not null,
    difficulty      tinyint(6)  not null default 0
);

create table maq (
    question_id    bigint,
    choice_1       varchar(255)     not null,
    choice_2       varchar(255)     not null,
    choice_3       varchar(255)     not null,
    choice_4       varchar(255)     not null,
    answer         tinyint(1)       not null,
    primary key (question_id),
    foreign key (question_id) references questions(question_id)
);

create table uaq (
     question_id     bigint          not null,
     answer         tinyint(1)       not null,
     primary key (question_id),
     foreign key (question_id) references questions(question_id)
);

create table saq (
     question_id    bigint           not null,
     keyword_1      varchar(255)     not null,
     keyword_2      varchar(255)     not null,
     keyword_3      varchar(255)     not null,
     answer         tinyint(1)       not null,
     primary key (question_id),
     foreign key (question_id) references questions(question_id)
);

############################## 시즌정보 관련 테이블 ##############################
create table season (
    id              bigint         primary key auto_increment not null,
    name            varchar(255)   null,
    description     varchar(255)   null,
    start_dt        datetime(6)    default current_timestamp(6),
    end_dt          datetime(6)    null
);

create table season_contest_questions (
  season_id       bigint         not null,
  question_id     bigint         not null,
  seq             int(11)        not null,
  primary key (season_id, question_id),
  foreign key (season_id) references season(id),
  foreign key (question_id) references questions(question_id)
);

################################ 히스토리 관련 테이블 ################################
create table question_history (
    id              bigint          primary key auto_increment not null,
    user_id         bigint          not null,
    question_id     bigint          not null,
    selected_answer tinyint(1)      not null,
    is_correct      boolean         not null,
    score_result    int(11)         not null default 0,
    created_dt      datetime(6)     default current_timestamp(6),

    foreign key (user_id) references users(user_id),
    foreign key (question_id) references questions(question_id)
);

create table contest_history (
    id              bigint          primary key auto_increment not null,
    user_id         bigint          not null,
    question_id     bigint          not null,
    selected_answer tinyint(1)      not null,
    is_correct      boolean         not null,
    score_result    int(11)         not null default 0,
    created_dt      datetime(6)     default current_timestamp(6),

    foreign key (user_id) references users(user_id),
    foreign key (question_id) references questions(question_id)
);

create table rank_history (
     id              bigint          primary key not null,
     user_id         bigint          not null,
     rank_no         int(11)         not null,
     season_id       bigint          not null,
     created_dt      datetime(6)     default current_timestamp(6),

     foreign key (user_id) references users(user_id)
);