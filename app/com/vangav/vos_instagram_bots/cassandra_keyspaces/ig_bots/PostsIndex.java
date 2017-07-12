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
 * PostsIndex represents
 *   Table [posts_index]
 *   in Keyspace [ig_bots]
 * 
 * Name: posts_index
 * Description:
 *   stores all bot posts 
 * 
 * Columns:
 *   year_month_day : varchar
 *   airport_code : varchar
 *   seq_id : int
 *   post_id : uuid

 * Partition Keys: year_month_day, airport_code, seq_id
 * Secondary Keys: 
 * Caching: ALL
 * Order By:

 * Queries:
 *   - Name: insert
 *   Description:
 *     inserts a new post 
 *   Prepared Statement:
 *     INSERT INTO ig_bots.posts_index (year_month_day, airport_code, 
 *     seq_id, post_id) VALUES (:year_month_day, :airport_code, 
 *     :seq_id, :post_id); 
 *   - Name: select_post
 *   Description:
 *     selects a post's id 
 *   Prepared Statement:
 *     SELECT post_id FROM ig_bots.posts_index WHERE year_month_day = 
 *     :year_month_day AND airport_code = :airport_code AND seq_id 
 *     = :seq_id; 
 * */
public class PostsIndex extends Table {

  private static final String kKeySpaceName =
    "ig_bots";
  private static final String kTableName =
    "posts_index";

  public static final String kYearMonthDayColumnName =
    "year_month_day";
  public static final String kAirportCodeColumnName =
    "airport_code";
  public static final String kSeqIdColumnName =
    "seq_id";
  public static final String kPostIdColumnName =
    "post_id";

  /**
   * Query:
   * Name: insert
   * Description:
   *   inserts a new post 
   * Prepared Statement:
   *   INSERT INTO ig_bots.posts_index (year_month_day, airport_code, 
   *   seq_id, post_id) VALUES (:year_month_day, :airport_code, 
   *   :seq_id, :post_id); 
   */
  private static final String kInsertName =
    "insert";
  private static final String kInsertDescription =
    "inserts a new post ";
  private static final String kInsertPreparedStatement =
    "INSERT INTO ig_bots.posts_index (year_month_day, airport_code, seq_id, "
    + "post_id) VALUES (:year_month_day, :airport_code, :seq_id, "
    + ":post_id); ";

  /**
   * Query:
   * Name: select_post
   * Description:
   *   selects a post's id 
   * Prepared Statement:
   *   SELECT post_id FROM ig_bots.posts_index WHERE year_month_day = 
   *   :year_month_day AND airport_code = :airport_code AND seq_id 
   *   = :seq_id; 
   */
  private static final String kSelectPostName =
    "select_post";
  private static final String kSelectPostDescription =
    "selects a post's id ";
  private static final String kSelectPostPreparedStatement =
    "SELECT post_id FROM ig_bots.posts_index WHERE year_month_day = "
    + ":year_month_day AND airport_code = :airport_code AND seq_id = "
    + ":seq_id; ";

  /**
   * Constructor PostsIndex
   * @return new PostsIndex Object
   * @throws Exception
   */
  private PostsIndex () throws Exception {

    super (
      kKeySpaceName,
      kTableName,
      new Query (
        kInsertDescription,
        kInsertName,
        kInsertPreparedStatement),
      new Query (
        kSelectPostDescription,
        kSelectPostName,
        kSelectPostPreparedStatement));
  }

  private static PostsIndex instance = null;

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

      instance = new PostsIndex();
    }
  }

  /**
   * i
   * @return singleton static instance of PostsIndex
   * @throws Exception
   */
  public static PostsIndex i () throws Exception {

    if (instance == null) {

      instance = new PostsIndex();
    }

    return instance;
  }

  // Query: Insert
  // Description:
  //   inserts a new post 
  // Parepared Statement:
  //   INSERT INTO ig_bots.posts_index (year_month_day, airport_code, 
  //   seq_id, post_id) VALUES (:year_month_day, :airport_code, 
  //   :seq_id, :post_id); 

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
   * @param postid
   * @return Insert Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object postid) throws Exception {

    return
      this.getQueryDispatchable(
        kInsertName,
        yearmonthday,
        airportcode,
        seqid,
        postid);
  }

  /**
   * getBoundStatementInsert
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @param postid
   * @return Insert Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object postid) throws Exception {

    return
      this.getQuery(kInsertName).getBoundStatement(
        yearmonthday,
        airportcode,
        seqid,
        postid);
  }

  /**
   * executeAsyncInsert
   * executes Insert Query asynchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @param postid
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object postid) throws Exception {

    return
      this.getQuery(kInsertName).executeAsync(
        yearmonthday,
        airportcode,
        seqid,
        postid);
  }

  /**
   * executeSyncInsert
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes Insert Query synchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @param postid
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncInsert (
    Object yearmonthday,
    Object airportcode,
    Object seqid,
    Object postid) throws Exception {

    return
      this.getQuery(kInsertName).executeSync(
        yearmonthday,
        airportcode,
        seqid,
        postid);
  }

  // Query: SelectPost
  // Description:
  //   selects a post's id 
  // Parepared Statement:
  //   SELECT post_id FROM ig_bots.posts_index WHERE year_month_day = 
  //   :year_month_day AND airport_code = :airport_code AND seq_id 
  //   = :seq_id; 

  /**
   * getQuerySelectPost
   * @return SelectPost Query in the form of
   *           a Query Object
   * @throws Exception
   */
  public Query getQuerySelectPost (
    ) throws Exception {

    return this.getQuery(kSelectPostName);
  }

  /**
   * getQueryDispatchableSelectPost
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return SelectPost Query in the form of
   *           a QueryDisbatchable Object
   *           (e.g.: to be passed on to a worker instance)
   * @throws Exception
   */
  public QueryDispatchable getQueryDispatchableSelectPost (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQueryDispatchable(
        kSelectPostName,
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * getBoundStatementSelectPost
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return SelectPost Query in the form of
   *           a BoundStatement ready for execution or to be added to
   *           a BatchStatement
   * @throws Exception
   */
  public BoundStatement getBoundStatementSelectPost (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kSelectPostName).getBoundStatement(
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * executeAsyncSelectPost
   * executes SelectPost Query asynchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return ResultSetFuture
   * @throws Exception
   */
  public ResultSetFuture executeAsyncSelectPost (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kSelectPostName).executeAsync(
        yearmonthday,
        airportcode,
        seqid);
  }

  /**
   * executeSyncSelectPost
   * BLOCKING-METHOD: blocks till the ResultSet is ready
   * executes SelectPost Query synchronously
   * @param yearmonthday
   * @param airportcode
   * @param seqid
   * @return ResultSet
   * @throws Exception
   */
  public ResultSet executeSyncSelectPost (
    Object yearmonthday,
    Object airportcode,
    Object seqid) throws Exception {

    return
      this.getQuery(kSelectPostName).executeSync(
        yearmonthday,
        airportcode,
        seqid);
  }

}
