// GENERATED using CqlScriptsGeneratorMain.java
// Keyspace: ig_bots

CREATE KEYSPACE IF NOT EXISTS ig_bots
  WITH replication = {
    'class': 'SimpleStrategy', 'replication_factor' : 1
};

USE ig_bots;


CREATE TABLE IF NOT EXISTS cities (
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


CREATE TABLE IF NOT EXISTS users_index (
  year_month_day varchar,
  airport_code varchar,
  seq_id int,
  user_id uuid,
  last_active_year_month_day varchar,
  PRIMARY KEY (year_month_day, airport_code, seq_id) )
  WITH CACHING = 'ALL';


CREATE TABLE IF NOT EXISTS users_count (
  year_month_day_airport_code varchar,
  users_count counter,
  PRIMARY KEY (year_month_day_airport_code) )
  WITH CACHING = 'ALL';


CREATE TABLE IF NOT EXISTS posts_index (
  year_month_day varchar,
  airport_code varchar,
  seq_id int,
  post_id uuid,
  PRIMARY KEY (year_month_day, airport_code, seq_id) )
  WITH CACHING = 'ALL';


CREATE TABLE IF NOT EXISTS posts_count (
  year_month_day_airport_code varchar,
  posts_count counter,
  PRIMARY KEY (year_month_day_airport_code) )
  WITH CACHING = 'ALL';


