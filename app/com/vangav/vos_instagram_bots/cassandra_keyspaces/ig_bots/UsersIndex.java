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
 * UsersIndex represents
 *   Table [users_index]
 *   in Keyspace [ig_bots]
 * 
 * Name: users_index
 * Description:
 *   stores all bot users 
 * 
 * Columns:
 *   year_month_day : varchar
 *   airport_code : varchar
 *   seq_id : int
 *   user_id : uuid
 *   last_active_year_month_day : varchar

 * Partition Keys: year_month_day, airport_code, seq_id
 * Secondary Keys: 
 * Caching: ALL
 * Order By:

 * Queries:
 *   - Name: insert
 *   Description:
 *     inserts a new user 
 *   Prepared Statement:
 *     INSERT INTO ig_bots.users_index (year_month_day, airport_code, 
 *     seq_id, user_id, last_active_year_month_day) VALUES 
 *     (:year_month_day, :airport_code, :seq_id, :user_id, 
 *     :last_active_year_month_day); 
 *   - Name: select_city
 *   Description:
 *     selects all user's in a city created on a specific day 
 *   Prepared Statement:
 *     SELECT user_id FROM ig_bots.users_index WHERE year_month_day = 
 *     :year_month_day AND airport_code = :airport_code; 
 *   - Name: select_user
 *   Description:
 *     select a user's user id and last active date 
 *   Prepared Statement:
 *     SELECT user_id, last_active_year_month_day FROM ig_bots.users_index 
 *     WHERE year_month_day = :year_month_day AND airport_code = 
 *     :airport_code AND seq_id = :seq_id; 
 *   - Name: update_last_active_date
 *   Description:
 *     updates a user's last active_date 
 *   Prepared Statement:
 *     UPDATE ig_bots.users_index SET last_active_year_month_day = 
 *     :last_active_year_month_day WHERE ear_month_day = 
 *     :year_month_day AND airport_code = :airport_code AND seq_id 
 *     = :seq_id; 
 * */
public class UsersIndex extends Table {

  private static final String kKeySpaceName =
    "ig_bots";
  private static final String kTableName =
    "users_index";

  public static final String kYearMonthDayColumnName =
    "year_month_day";
  public static final String kAirportCodeColumnName =
    "airport_code";
  public static final String kSeqIdColumnName =
    "seq_id";
  public static final String kUserIdColumnName =
    "user_id";
  public static final String kLastActiveYearMonthDayColumnName =
    "last_active_year_month_day";

  /**
   * Query:
   * Name: insert
   * Description:
   *   inserts a new user 
   * Prepared Statement:
   *   INSERT INTO ig_bots.users_index (year_month_day, airport_code, 
   *   seq_id, user_id, last_active_year_month_day) VALUES 
   *   (:year_month_day, :airport_code, :seq_id, :user_id, 
   *   :last_active_year_month_day); 
   */
  private static final String kInsertName =
    "insert";
  private static final String kInsertDescription =
    "inserts a new user ";
  private static final String kInsertPreparedStatement =
    "INSERT INTO ig_bots.users_index (year_month_day, airport_code, seq_id, "
    + "user_id, last_active_year_month_day) VALUES (:year_month_day, "
    + ":airport_code, :seq_id, :user_id, :last_active_year_month_day); ";

  /**
   * Query:
   * Name: select_city
   * Description:
   *   selects all user's in a city created on a specific day 
   * Prepared Statement:
   *   SELECT user_id FROM ig_bots.users_index WHERE year_month_day = 
   *   :year_month_day AND airport_code = :airport_code; 
   */
  private static final String kSelectCityName =
    "select_city";
  private static final String kSelectCityDescription =
    "selects all user's in a city created on a specific day ";
  private static final String kSelectCityPreparedStatement =
    "SELECT user_id FROM ig_bots.users_index WHERE year_month_day = "
    + ":year_month_day AND airport_code = :airport_code; ";

  /**
   * Query:
   * Name: select_user
   * Description:
   *   select a user's user id and last active date 
   * Prepared Statement:
   *   SELECT user_id, last_active_year_month_day FROM ig_bots.users_index 
   *   WHERE year_month_day = :year_month_day AND airport_code = 
   *   :airport_code AND seq_id = :seq_id; 
   */
  private static final String kSelectUserName =
    "select_user";
  private static final String kSelectUserDescription =
    "select a user's user id and last active date ";
  private static final String kSelectUserPreparedStatement =
    "SELECT user_id, last_active_year_month_day FROM ig_bots.users_index "
    + "WHERE year_month_day = :year_month_day AND airport_code = "
    + ":airport_code AND seq_id = :seq_id; ";

  /**
   * Query:
   * Name: update_last_active_date
   * Description:
   *   updates a user's last active_date 
   * Prepared Statement:
   *   UPDATE ig_bots.users_index SET last_active_year_month_day = 
   *   :last_active_year_month_day WHERE ear_month_day = 
   *   :year_month_day AND airport_code = :airport_code AND seq_id 
   *   = :seq_id; 
   */
  private static final String kUpdateLastActiveDateName =
    "update_last_active_date";
  private static final String kUpdateLastActiveDateDescription =
    "updates a user's last active_date ";
  private static final String kUpdateLastActiveDatePreparedStatement =
    "UPDATE ig_bots.users_index SET last_active_year_month_day = "
    + ":last_active_year_month_day WHERE ear_month_day = "
    + ":year_month_day AND airport_code = :airport_code AND seq_id = "
    + ":seq_id; ";

  /**
   * Constructor UsersIndex
   * @return new UsersIndex Object
   * @throws Exception
   */
  private UsersIndex () throws Exception {

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
        kSelectUserDescription,
        kSelectUserName,
        kSelectUserPreparedStatement),
      new Query (
        kUpdateLastActiveDateDescription,
        kUpdateLastActiveDateName,
        kUpdateLastActiveDatePreparedStatement));
  }

  private static UsersIndex instance = null;

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

      instance = new UsersIndex();
    }
  }

  /**
   * i
   * @return singleton static instance of UsersIndex
   * @throws Exception
   */
  public static UsersIndex i () throws Exception {

    if (instance == null) {

      instance = new UsersIndex();
    }

    return instance;
  }

  // Query: Insert
  // Description:
  //   inserts a new user 
  // Parepared Statement:
  //   INSERT INTO ig_bots.users_index (year_month_day, airport_code, 
  //   seq_id, user_id, last_active_year_month_day) VALUES 
  //   (:year_month_day, :airport_code, :seq_id, :user_id, 
  //   :last_active_year_month_day); 

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
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @param userid
   * @param lastactiveyearmonthday
   * @return Insert Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object userid,
    Object lastactiveyearmonthday) throws Exception {

    return
      this.getQueryDispatchable(
        kInsertName,
        yearmonthday,
        airportcode,
        seqid,
        userid,
        lastactiveyearmonthday);
  }

  /**
   * getBoundStatementInsert
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @param userid
   * @param lastactiveyearmonthday
   * @return Insert Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object userid,
    Object lastactiveyearmonthday) throws Exception {

    return
      this.getQuery(kInsertName).getBoundStatement(
        yearmonthday,
        airportcode,
        seqid,
        userid,
        lastactiveyearmonthday);
  }

  /**
   * executeAsyncInsert
   * executes Insert Query asynchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @param userid
   * @param lastactiveyearmonthday
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object userid,
    Object lastactiveyearmonthday) throws Exception {

    return
      this.getQuery(kInsertName).executeAsync(
        yearmonthday,
        airportcode,
        seqid,
        userid,
        lastactiveyearmonthday);
  }

  /**
   * executeSyncInsert
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes Insert Query synchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @param userid
   * @param lastactiveyearmonthday
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object userid,
    Object lastactiveyearmonthday) throws Exception {

    return
      this.getQuery(kInsertName).executeSync(
        yearmonthday,
        airportcode,
        seqid,
        userid,
        lastactiveyearmonthday);
  }

  // Query: SelectCity
  // Description:
  //   selects all user's in a city created on a specific day 
  // Parepared Statement:
  //   SELECT user_id FROM ig_bots.users_index WHERE year_month_day = 
  //   :year_month_day AND airport_code = :airport_code; 

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
   * @param yearmonthday
   * @param airportcode
   * @return SelectCity Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableSelectCity (
    Object yearmonthday,
    Object airportcode) throws Exception {

    return
      this.getQueryDispatchable(
        kSelectCityName,
        yearmonthday,
        airportcode);
  }

  /**
   * getBoundStatementSelectCity
   * @param yearmonthday
   * @param airportcode
   * @return SelectCity Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementSelectCity (
    Object yearmonthday,
    Object airportcode) throws Exception {

    return
      this.getQuery(kSelectCityName).getBoundStatement(
        yearmonthday,
        airportcode);
  }

  /**
   * executeAsyncSelectCity
   * executes SelectCity Query asynchronously
   * @param yearmonthday
   * @param airportcode
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncSelectCity (
    Object yearmonthday,
    Object airportcode) throws Exception {

    return
      this.getQuery(kSelectCityName).executeAsync(
        yearmonthday,
        airportcode);
  }

  /**
   * executeSyncSelectCity
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes SelectCity Query synchronously
   * @param yearmonthday
   * @param airportcode
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncSelectCity (
    Object yearmonthday,
    Object airportcode) throws Exception {

    return
      this.getQuery(kSelectCityName).executeSync(
        yearmonthday,
        airportcode);
  }

  // Query: SelectUser
  // Description:
  //   select a user's user id and last active date 
  // Parepared Statement:
  //   SELECT user_id, last_active_year_month_day FROM ig_bots.users_index 
  //   WHERE year_month_day = :year_month_day AND airport_code = 
  //   :airport_code AND seq_id = :seq_id; 

  /**
   * getQuerySelectUser
   * @return SelectUser Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQuerySelectUser (
    ) throws Exception {

    return this.getQuery(kSelectUserName);
  }

  /**
   * getQueryDispatchableSelectUser
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return SelectUser Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableSelectUser (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQueryDispatchable(
        kSelectUserName,
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * getBoundStatementSelectUser
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return SelectUser Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementSelectUser (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kSelectUserName).getBoundStatement(
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * executeAsyncSelectUser
   * executes SelectUser Query asynchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncSelectUser (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kSelectUserName).executeAsync(
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * executeSyncSelectUser
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes SelectUser Query synchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncSelectUser (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kSelectUserName).executeSync(
        yearmonthday,
        airportcode,
        seqid);
  }

  // Query: UpdateLastActiveDate
  // Description:
  //   updates a user's last active_date 
  // Parepared Statement:
  //   UPDATE ig_bots.users_index SET last_active_year_month_day = 
  //   :last_active_year_month_day WHERE ear_month_day = 
  //   :year_month_day AND airport_code = :airport_code AND seq_id 
  //   = :seq_id; 

  /**
   * getQueryUpdateLastActiveDate
   * @return UpdateLastActiveDate Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQueryUpdateLastActiveDate (
    ) throws Exception {

    return this.getQuery(kUpdateLastActiveDateName);
  }

  /**
   * getQueryDispatchableUpdateLastActiveDate
   * @param lastactiveyearmonthday
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return UpdateLastActiveDate Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableUpdateLastActiveDate (
    Object lastactiveyearmonthday,
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQueryDispatchable(
        kUpdateLastActiveDateName,
        lastactiveyearmonthday,
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * getBoundStatementUpdateLastActiveDate
   * @param lastactiveyearmonthday
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return UpdateLastActiveDate Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementUpdateLastActiveDate (
    Object lastactiveyearmonthday,
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kUpdateLastActiveDateName).getBoundStatement(
        lastactiveyearmonthday,
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * executeAsyncUpdateLastActiveDate
   * executes UpdateLastActiveDate Query asynchronously
   * @param lastactiveyearmonthday
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncUpdateLastActiveDate (
    Object lastactiveyearmonthday,
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kUpdateLastActiveDateName).executeAsync(
        lastactiveyearmonthday,
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * executeSyncUpdateLastActiveDate
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes UpdateLastActiveDate Query synchronously
   * @param lastactiveyearmonthday
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncUpdateLastActiveDate (
    Object lastactiveyearmonthday,
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kUpdateLastActiveDateName).executeSync(
        lastactiveyearmonthday,
        yearmonthday,
        airportcode,
        seqid);
  }

}
