--- iam.myUser
DROP TABLE IF EXISTS iam.user;
DROP SEQUENCE IF EXISTS iam.user_id_seq;
DROP INDEX IF EXISTS iam.username_idx;

CREATE SEQUENCE iam.user_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10000 CACHE 1;
CREATE TABLE IF NOT EXISTS iam.user (
    id              INT NOT NULL DEFAULT nextval('iam.user_id_seq'::regclass),
    username        VARCHAR(45) NOT NULL,
    password        VARCHAR(45) NOT NULL,
    PRIMARY KEY (id));
CREATE UNIQUE INDEX IF NOT EXISTS username_idx ON iam.user(username);

--- iam.authorities
DROP TABLE IF EXISTS iam.authority;
DROP SEQUENCE IF EXISTS iam.authority_id_seq;

CREATE SEQUENCE iam.authority_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10000 CACHE 1;
CREATE TABLE IF NOT EXISTS iam.authority (
    id              INT NOT NULL DEFAULT nextval('iam.authority_id_seq'::regclass),
    authority       VARCHAR(45) NOT NULL,
    user_id         INT NOT NULL,
    PRIMARY KEY (id));

--- iam.client
DROP TABLE IF EXISTS iam.client;
DROP SEQUENCE IF EXISTS iam.client_id_seq;

CREATE SEQUENCE iam.client_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10000 CACHE 1;
CREATE TABLE IF NOT EXISTS iam.client (
    id                  INT NOT NULL DEFAULT nextval('iam.client_id_seq'::regclass),
    client              VARCHAR(45) NOT NULL,
    secret              VARCHAR(45) NOT NULL,
    redirect_uri        VARCHAR(45) NOT NULL,
    PRIMARY KEY (id));

--- iam.client_grant_type
DROP TABLE IF EXISTS iam.client_grant_type;
DROP SEQUENCE IF EXISTS iam.client_grant_type_id_seq;

CREATE SEQUENCE iam.client_grant_type_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10000 CACHE 1;
CREATE TABLE IF NOT EXISTS iam.client_grant_type (
    id                  INT NOT NULL DEFAULT nextval('iam.client_grant_type_id_seq'::regclass),
    grant_type          VARCHAR(45) NOT NULL,
    client_id           VARCHAR(45) NOT NULL,
    PRIMARY KEY (id));

--- iam.scope
DROP TABLE IF EXISTS iam.scope;
DROP SEQUENCE IF EXISTS iam.scope_id_seq;

CREATE SEQUENCE iam.scope_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10000 CACHE 1;
CREATE TABLE IF NOT EXISTS iam.scope (
     id                  INT NOT NULL DEFAULT nextval('iam.scope_id_seq'::regclass),
     scope               VARCHAR(45) NOT NULL,
     client_id           VARCHAR(45) NOT NULL,
     PRIMARY KEY (id));