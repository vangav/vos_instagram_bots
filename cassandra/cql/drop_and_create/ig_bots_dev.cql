// Keyspace: ig_bots

DROP KEYSPACE IF EXISTS ig_bots;

CREATE KEYSPACE ig_bots
  WITH replication = {
    'class': 'SimpleStrategy', 'replication_factor' : 1
};


USE ig_bots;

DROP TABLE IF EXISTS cities;

CREATE TABLE cities (
  airport_code varchar,
  latitude double,
  longitude double,
  continent varchar,
  continent_code varchar,
  country varchar,
  country_code varchar,
  city varchar,
  population_in_millions int,
  added_year_month_day varchar,
  PRIMARY KEY (airport_code) )
  WITH CACHING = 'ALL';


DROP TABLE IF EXISTS users_index;

CREATE TABLE users_index (
  year_month_day varchar,
  airport_code varchar,
  seq_id int,
  user_id uuid,
  last_active_year_month_day varchar,
  PRIMARY KEY (year_month_day, airport_code, seq_id) )
  WITH CACHING = 'ALL';


DROP TABLE IF EXISTS users_count;

CREATE TABLE users_count (
  year_month_day_airport_code varchar,
  users_count counter,
  PRIMARY KEY (year_month_day_airport_code) )
  WITH CACHING = 'ALL';


DROP TABLE IF EXISTS posts_index;

CREATE TABLE posts_index (
  year_month_day varchar,
  airport_code varchar,
  seq_id int,
  post_id uuid,
  PRIMARY KEY (year_month_day, airport_code, seq_id) )
  WITH CACHING = 'ALL';


DROP TABLE IF EXISTS posts_count;

CREATE TABLE posts_count (
  year_month_day_airport_code varchar,
  posts_count counter,
  PRIMARY KEY (year_month_day_airport_code) )
  WITH CACHING = 'ALL';


