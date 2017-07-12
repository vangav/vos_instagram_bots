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
 * PostsCount represents
 *   Table [posts_count]
 *   in Keyspace [ig_bots]
 * 
 * Name: posts_count
 * Description:
 *   stores the count of posts created on a day in a city 
 * 
 * Columns:
 *   year_month_day_airport_code : varchar
 *   posts_count : counter

 * Partition Keys: year_month_day_airport_code
 * Secondary Keys: 
 * Caching: ALL
 * Order By:

 * Queries:
 *   - Name: increment
 *   Description:
 *     increments the count of posts created on a day in a city 
 *   Prepared Statement:
 *     UPDATE ig_bots.posts_count SET posts_count = posts_count + 1 WHERE 
 *     year_month_day_airport_code = :year_month_day_airport_code; 
 *   - Name: increment_value
 *   Description:
 *     increments the count of posts created on a day in a city by a value 
 *   Prepared Statement:
 *     UPDATE ig_bots.posts_count SET posts_count = posts_count + 
 *     :posts_count WHERE year_month_day_airport_code = 
 *     :year_month_day_airport_code; 
 *   - Name: select
 *   Description:
 *     selects the count of posts created on a day in a city 
 *   Prepared Statement:
 *     SELECT posts_count FROM ig_bots.posts_count WHERE 
 *     year_month_day_airport_code = :year_month_day_airport_code; 
 * */
public class PostsCount extends Table {

  private static final String kKeySpaceName =
    "ig_bots";
  private static final String kTableName =
    "posts_count";

  public static final String kYearMonthDayAirportCodeColumnName =
    "year_month_day_airport_code";
  public static final String kPostsCountColumnName =
    "posts_count";

  /**
   * Query:
   * Name: increment
   * Description:
   *   increments the count of posts created on a day in a city 
   * Prepared Statement:
   *   UPDATE ig_bots.posts_count SET posts_count = posts_count + 1 WHERE 
   *   year_month_day_airport_code = :year_month_day_airport_code; 
   */
  private static final String kIncrementName =
    "increment";
  private static final String kIncrementDescription =
    "increments the count of posts created on a day in a city ";
  private static final String kIncrementPreparedStatement =
    "UPDATE ig_bots.posts_count SET posts_count = posts_count + 1 WHERE "
    + "year_month_day_airport_code = :year_month_day_airport_code; ";

  /**
   * Query:
   * Name: increment_value
   * Description:
   *   increments the count of posts created on a day in a city by a value 
   * Prepared Statement:
   *   UPDATE ig_bots.posts_count SET posts_count = posts_count + 
   *   :posts_count WHERE year_month_day_airport_code = 
   *   :year_month_day_airport_code; 
   */
  private static final String kIncrementValueName =
    "increment_value";
  private static final String kIncrementValueDescription =
    "increments the count of posts created on a day in a city by a value ";
  private static final String kIncrementValuePreparedStatement =
    "UPDATE ig_bots.posts_count SET posts_count = posts_count + "
    + ":posts_count WHERE year_month_day_airport_code = "
    + ":year_month_day_airport_code; ";

  /**
   * Query:
   * Name: select
   * Description:
   *   selects the count of posts created on a day in a city 
   * Prepared Statement:
   *   SELECT posts_count FROM ig_bots.posts_count WHERE 
   *   year_month_day_airport_code = :year_month_day_airport_code; 
   */
  private static final String kSelectName =
    "select";
  private static final String kSelectDescription =
    "selects the count of posts created on a day in a city ";
  private static final String kSelectPreparedStatement =
    "SELECT posts_count FROM ig_bots.posts_count WHERE "
    + "year_month_day_airport_code = :year_month_day_airport_code; ";

  /**
   * Constructor PostsCount
   * @return new PostsCount Object
   * @throws Exception
   */
  private PostsCount () throws Exception {

    super (
      kKeySpaceName,
      kTableName,
      new Query (
        kIncrementDescription,
        kIncrementName,
        kIncrementPreparedStatement),
      new Query (
        kIncrementValueDescription,
        kIncrementValueName,
        kIncrementValuePreparedStatement),
      new Query (
        kSelectDescription,
        kSelectName,
        kSelectPreparedStatement));
  }

  private static PostsCount instance = null;

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

      instance = new PostsCount();
    }
  }

  /**
   * i
   * @return singleton static instance of PostsCount
   * @throws Exception
   */
  public static PostsCount i () throws Exception {

    if (instance == null) {

      instance = new PostsCount();
    }

    return instance;
  }

  // Query: Increment
  // Description:
  //   increments the count of posts created on a day in a city 
  // Parepared Statement:
  //   UPDATE ig_bots.posts_count SET posts_count = posts_count + 1 WHERE 
  //   year_month_day_airport_code = :year_month_day_airport_code; 

  /**
   * getQueryIncrement
   * @return Increment Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQueryIncrement (
    ) throws Exception {

    return this.getQuery(kIncrementName);
  }

  /**
   * getQueryDispatchableIncrement
   * @param yearmonthdayairportcode
   * @return Increment Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableIncrement (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQueryDispatchable(
        kIncrementName,
        yearmonthdayairportcode);
  }

  /**
   * getBoundStatementIncrement
   * @param yearmonthdayairportcode
   * @return Increment Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementIncrement (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kIncrementName).getBoundStatement(
        yearmonthdayairportcode);
  }

  /**
   * executeAsyncIncrement
   * executes Increment Query asynchronously
   * @param yearmonthdayairportcode
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncIncrement (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kIncrementName).executeAsync(
        yearmonthdayairportcode);
  }

  /**
   * executeSyncIncrement
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes Increment Query synchronously
   * @param yearmonthdayairportcode
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncIncrement (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kIncrementName).executeSync(
        yearmonthdayairportcode);
  }

  // Query: IncrementValue
  // Description:
  //   increments the count of posts created on a day in a city by a value 
  // Parepared Statement:
  //   UPDATE ig_bots.posts_count SET posts_count = posts_count + 
  //   :posts_count WHERE year_month_day_airport_code = 
  //   :year_month_day_airport_code; 

  /**
   * getQueryIncrementValue
   * @return IncrementValue Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQueryIncrementValue (
    ) throws Exception {

    return this.getQuery(kIncrementValueName);
  }

  /**
   * getQueryDispatchableIncrementValue
   * @param postscount
   * @param yearmonthdayairportcode
   * @return IncrementValue Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableIncrementValue (
    Object postscount,
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQueryDispatchable(
        kIncrementValueName,
        postscount,
        yearmonthdayairportcode);
  }

  /**
   * getBoundStatementIncrementValue
   * @param postscount
   * @param yearmonthdayairportcode
   * @return IncrementValue Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementIncrementValue (
    Object postscount,
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kIncrementValueName).getBoundStatement(
        postscount,
        yearmonthdayairportcode);
  }

  /**
   * executeAsyncIncrementValue
   * executes IncrementValue Query asynchronously
   * @param postscount
   * @param yearmonthdayairportcode
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncIncrementValue (
    Object postscount,
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kIncrementValueName).executeAsync(
        postscount,
        yearmonthdayairportcode);
  }

  /**
   * executeSyncIncrementValue
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes IncrementValue Query synchronously
   * @param postscount
   * @param yearmonthdayairportcode
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncIncrementValue (
    Object postscount,
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kIncrementValueName).executeSync(
        postscount,
        yearmonthdayairportcode);
  }

  // Query: Select
  // Description:
  //   selects the count of posts created on a day in a city 
  // Parepared Statement:
  //   SELECT posts_count FROM ig_bots.posts_count WHERE 
  //   year_month_day_airport_code = :year_month_day_airport_code; 

  /**
   * getQuerySelect
   * @return Select Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQuerySelect (
    ) throws Exception {

    return this.getQuery(kSelectName);
  }

  /**
   * getQueryDispatchableSelect
   * @param yearmonthdayairportcode
   * @return Select Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableSelect (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQueryDispatchable(
        kSelectName,
        yearmonthdayairportcode);
  }

  /**
   * getBoundStatementSelect
   * @param yearmonthdayairportcode
   * @return Select Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementSelect (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kSelectName).getBoundStatement(
        yearmonthdayairportcode);
  }

  /**
   * executeAsyncSelect
   * executes Select Query asynchronously
   * @param yearmonthdayairportcode
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncSelect (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kSelectName).executeAsync(
        yearmonthdayairportcode);
  }

  /**
   * executeSyncSelect
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes Select Query synchronously
   * @param yearmonthdayairportcode
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncSelect (
    Object yearmonthdayairportcode) throws Exception {

    return
      this.getQuery(kSelectName).executeSync(
        yearmonthdayairportcode);
  }

}
