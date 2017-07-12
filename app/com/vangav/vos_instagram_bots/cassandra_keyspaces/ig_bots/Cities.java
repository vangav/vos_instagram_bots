/**
 * "First, solve the problem. Then, write the code. -John Johnson"
 * "Or use Vangav M"
 * www.vangav.com
 * */

/**
 * MIT License
 *
 * Copyright (c) 2016 Vangav
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 * */

/**
 * Community
 * Facebook Group: Vangav Open Source - Backend
 *   fb.com/groups/575834775932682/
 * Facebook Page: Vangav
 *   fb.com/vangav.f
 * 
 * Third party communities for Vangav Backend
 *   - play framework
 *   - cassandra
 *   - datastax
 *   
 * Tag your question online (e.g.: stack overflow, etc ...) with
 *   #vangav_backend
 *   to easier find questions/answers online
 * */

package com.vangav.vos_instagram_bots.cassandra_keyspaces.ig_bots;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.vangav.backend.cassandra.keyspaces.Query;
import com.vangav.backend.cassandra.keyspaces.Table;
import com.vangav.backend.cassandra.keyspaces.dispatch_message.QueryDispatchable;

/**
 * GENERATED using JavaClientGeneratorMain.java
 */
/**
 * Cities represents
 *   Table [cities]
 *   in Keyspace [ig_bots]
 * 
 * Name: cities
 * Description:
 *   stores cities in which bot users are created 
 * 
 * Columns:
 *   airport_code : varchar
 *   latitude : double
 *   longitude : double
 *   continent : varchar
 *   continent_code : varchar
 *   country : varchar
 *   country_code : varchar
 *   city : varchar
 *   population_in_millions : int
 *   added_year_month_day : varchar

 * Partition Keys: airport_code
 * Secondary Keys: 
 * Caching: ALL
 * Order By:

 * Queries:
 *   - Name: insert
 *   Description:
 *     inserts a new city 
 *   Prepared Statement:
 *     INSERT INTO ig_bots.cities (airport_code, latitude, longitude, 
 *     continent, continent_code, country, country_code, city, 
 *     population_in_millions, added_year_month_day) VALUES 
 *     (:airport_code, :latitude, :longitude, :continent, 
 *     :continent_code, :country, :country_code, :city, 
 *     :population_in_millions, :added_year_month_day); 
 *   - Name: select_city
 *   Description:
 *     selects one city's attributes 
 *   Prepared Statement:
 *     SELECT latitude, longitude, continent, continent_code, country, 
 *     country_code, city, population_in_millions, 
 *     added_year_month_day FROM ig_bots.cities WHERE airport_code 
 *     = :airport_code; 
 *   - Name: select_all
 *   Description:
 *     selects all cities attributes 
 *   Prepared Statement:
 *     SELECT * from ig_bots.cities; 
 * */
public class Cities extends Table {

  private static final String kKeySpaceName =
    "ig_bots";
  private static final String kTableName =
    "cities";

  public static final String kAirportCodeColumnName =
    "airport_code";
  public static final String kLatitudeColumnName =
    "latitude";
  public static final String kLongitudeColumnName =
    "longitude";
  public static final String kContinentColumnName =
    "continent";
  public static final String kContinentCodeColumnName =
    "continent_code";
  public static final String kCountryColumnName =
    "country";
  public static final String kCountryCodeColumnName =
    "country_code";
  public static final String kCityColumnName =
    "city";
  public static final String kPopulationInMillionsColumnName =
    "population_in_millions";
  public static final String kAddedYearMonthDayColumnName =
    "added_year_month_day";

  /**
   * Query:
   * Name: insert
   * Description:
   *   inserts a new city 
   * Prepared Statement:
   *   INSERT INTO ig_bots.cities (airport_code, latitude, longitude, 
   *   continent, continent_code, country, country_code, city, 
   *   population_in_millions, added_year_month_day) VALUES 
   *   (:airport_code, :latitude, :longitude, :continent, 
   *   :continent_code, :country, :country_code, :city, 
   *   :population_in_millions, :added_year_month_day); 
   */
  private static final String kInsertName =
    "insert";
  private static final String kInsertDescription =
    "inserts a new city ";
  private static final String kInsertPreparedStatement =
    "INSERT INTO ig_bots.cities (airport_code, latitude, longitude, "
    + "continent, continent_code, country, country_code, city, "
    + "population_in_millions, added_year_month_day) VALUES "
    + "(:airport_code, :latitude, :longitude, :continent, "
    + ":continent_code, :country, :country_code, :city, "
    + ":population_in_millions, :added_year_month_day); ";

  /**
   * Query:
   * Name: select_city
   * Description:
   *   selects one city's attributes 
   * Prepared Statement:
   *   SELECT latitude, longitude, continent, continent_code, country, 
   *   country_code, city, population_in_millions, 
   *   added_year_month_day FROM ig_bots.cities WHERE airport_code 
   *   = :airport_code; 
   */
  private static final String kSelectCityName =
    "select_city";
  private static final String kSelectCityDescription =
    "selects one city's attributes ";
  private static final String kSelectCityPreparedStatement =
    "SELECT latitude, longitude, continent, continent_code, country, "
    + "country_code, city, population_in_millions, "
    + "added_year_month_day FROM ig_bots.cities WHERE airport_code = "
    + ":airport_code; ";

  /**
   * Query:
   * Name: select_all
   * Description:
   *   selects all cities attributes 
   * Prepared Statement:
   *   SELECT * from ig_bots.cities; 
   */
  private static final String kSelectAllName =
    "select_all";
  private static final String kSelectAllDescription =
    "selects all cities attributes ";
  private static final String kSelectAllPreparedStatement =
    "SELECT * from ig_bots.cities; ";

  /**
   * Constructor Cities
   * @return new Cities Object
   * @throws Exception
   */
  private Cities () throws Exception {

    super (
      kKeySpaceName,
      kTableName,
      new Query (
        kInsertDescription,
        kInsertName,
        kInsertPreparedStatement),
      new Query (
        kSelectCityDescription,
        kSelectCityName,
        kSelectCityPreparedStatement),
      new Query (
        kSelectAllDescription,
        kSelectAllName,
        kSelectAllPreparedStatement));
  }

  private static Cities instance = null;

  /**
   * loadTable
   * OPTIONAL method
   * instance is created either upon calling this method or upon the first call
   *   to singleton instance method i
   * this method is useful for loading upon program start instead of loading
   *   it upon the first use since there's a small time overhead for loading
   *   since all queries are prepared synchronously in a blocking network
   *   operation with Cassandra's server
   * @throws Exception
   */
  public static void loadTable () throws Exception {

    if (instance == null) {

      instance = new Cities();
    }
  }

  /**
   * i
   * @return singleton static instance of Cities
   * @throws Exception
   */
  public static Cities i () throws Exception {

    if (instance == null) {

      instance = new Cities();
    }

    return instance;
  }

  // Query: Insert
  // Description:
  //   inserts a new city 
  // Parepared Statement:
  //   INSERT INTO ig_bots.cities (airport_code, latitude, longitude, 
  //   continent, continent_code, country, country_code, city, 
  //   population_in_millions, added_year_month_day) VALUES 
  //   (:airport_code, :latitude, :longitude, :continent, 
  //   :continent_code, :country, :country_code, :city, 
  //   :population_in_millions, :added_year_month_day); 

  /**
   * getQueryInsert
   * @return Insert Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQueryInsert (
    ) throws Exception {

    return this.getQuery(kInsertName);
  }

  /**
   * getQueryDispatchableInsert
   * @param airportcode
   * @param latitude
   * @param longitude
   * @param continent
   * @param continentcode
   * @param country
   * @param countrycode
   * @param city
   * @param populationinmillions
   * @param addedyearmonthday
   * @return Insert Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableInsert (
    Object airportcode,
    Object latitude,
    Object longitude,
    Object continent,
    Object continentcode,
    Object country,
    Object countrycode,
    Object city,
    Object populationinmillions,
    Object addedyearmonthday) throws Exception {

    return
      this.getQueryDispatchable(
        kInsertName,
        airportcode,
        latitude,
        longitude,
        continent,
        continentcode,
        country,
        countrycode,
        city,
        populationinmillions,
        addedyearmonthday);
  }

  /**
   * getBoundStatementInsert
   * @param airportcode
   * @param latitude
   * @param longitude
   * @param continent
   * @param continentcode
   * @param country
   * @param countrycode
   * @param city
   * @param populationinmillions
   * @param addedyearmonthday
   * @return Insert Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementInsert (
    Object airportcode,
    Object latitude,
    Object longitude,
    Object continent,
    Object continentcode,
    Object country,
    Object countrycode,
    Object city,
    Object populationinmillions,
    Object addedyearmonthday) throws Exception {

    return
      this.getQuery(kInsertName).getBoundStatement(
        airportcode,
        latitude,
        longitude,
        continent,
        continentcode,
        country,
        countrycode,
        city,
        populationinmillions,
        addedyearmonthday);
  }

  /**
   * executeAsyncInsert
   * executes Insert Query asynchronously
   * @param airportcode
   * @param latitude
   * @param longitude
   * @param continent
   * @param continentcode
   * @param country
   * @param countrycode
   * @param city
   * @param populationinmillions
   * @param addedyearmonthday
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncInsert (
    Object airportcode,
    Object latitude,
    Object longitude,
    Object continent,
    Object continentcode,
    Object country,
    Object countrycode,
    Object city,
    Object populationinmillions,
    Object addedyearmonthday) throws Exception {

    return
      this.getQuery(kInsertName).executeAsync(
        airportcode,
        latitude,
        longitude,
        continent,
        continentcode,
        country,
        countrycode,
        city,
        populationinmillions,
        addedyearmonthday);
  }

  /**
   * executeSyncInsert
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes Insert Query synchronously
   * @param airportcode
   * @param latitude
   * @param longitude
   * @param continent
   * @param continentcode
   * @param country
   * @param countrycode
   * @param city
   * @param populationinmillions
   * @param addedyearmonthday
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncInsert (
    Object airportcode,
    Object latitude,
    Object longitude,
    Object continent,
    Object continentcode,
    Object country,
    Object countrycode,
    Object city,
    Object populationinmillions,
    Object addedyearmonthday) throws Exception {

    return
      this.getQuery(kInsertName).executeSync(
        airportcode,
        latitude,
        longitude,
        continent,
        continentcode,
        country,
        countrycode,
        city,
        populationinmillions,
        addedyearmonthday);
  }

  // Query: SelectCity
  // Description:
  //   selects one city's attributes 
  // Parepared Statement:
  //   SELECT latitude, longitude, continent, continent_code, country, 
  //   country_code, city, population_in_millions, 
  //   added_year_month_day FROM ig_bots.cities WHERE airport_code 
  //   = :airport_code; 

  /**
   * getQuerySelectCity
   * @return SelectCity Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQuerySelectCity (
    ) throws Exception {

    return this.getQuery(kSelectCityName);
  }

  /**
   * getQueryDispatchableSelectCity
   * @param airportcode
   * @return SelectCity Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableSelectCity (
    Object airportcode) throws Exception {

    return
      this.getQueryDispatchable(
        kSelectCityName,
        airportcode);
  }

  /**
   * getBoundStatementSelectCity
   * @param airportcode
   * @return SelectCity Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementSelectCity (
    Object airportcode) throws Exception {

    return
      this.getQuery(kSelectCityName).getBoundStatement(
        airportcode);
  }

  /**
   * executeAsyncSelectCity
   * executes SelectCity Query asynchronously
   * @param airportcode
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncSelectCity (
    Object airportcode) throws Exception {

    return
      this.getQuery(kSelectCityName).executeAsync(
        airportcode);
  }

  /**
   * executeSyncSelectCity
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes SelectCity Query synchronously
   * @param airportcode
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncSelectCity (
    Object airportcode) throws Exception {

    return
      this.getQuery(kSelectCityName).executeSync(
        airportcode);
  }

  // Query: SelectAll
  // Description:
  //   selects all cities attributes 
  // Parepared Statement:
  //   SELECT * from ig_bots.cities; 

  /**
   * getQuerySelectAll
   * @return SelectAll Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQuerySelectAll (
    ) throws Exception {

    return this.getQuery(kSelectAllName);
  }

  /**
   * getQueryDispatchableSelectAll
   * @return SelectAll Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableSelectAll (
    ) throws Exception {

    return
      this.getQueryDispatchable(
        kSelectAllName);
  }

  /**
   * getBoundStatementSelectAll
   * @return SelectAll Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementSelectAll (
    ) throws Exception {

    return
      this.getQuery(kSelectAllName).getBoundStatement();
  }

  /**
   * executeAsyncSelectAll
   * executes SelectAll Query asynchronously
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncSelectAll (
    ) throws Exception {

    return
      this.getQuery(kSelectAllName).executeAsync();
  }

  /**
   * executeSyncSelectAll
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes SelectAll Query synchronously
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncSelectAll (
    ) throws Exception {

    return
      this.getQuery(kSelectAllName).executeSync();
  }

}
