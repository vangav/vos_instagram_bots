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

package com.vangav.vos_instagram_bots.clients.vos_instagram.get_followers;

import com.vangav.backend.networks.rest_client.RestRequestGetQuery;

/**
 * Generated using ClientsGeneratorInl
 */
/**
 * RequestGetFollowers represents the request to
 *   be sent to GetFollowers controller
 */
public class RequestGetFollowers extends RestRequestGetQuery {

  /**
   * Constructor - RequestGetFollowers
   * this is the constructor for mandatory request's params only
   * @param devicetoken
   * @param userid
   * @param accesstoken
   * @param getfollowersuserid
   * @return new RequestGetFollowers Object
   * @throws Exception
   */
  public RequestGetFollowers (
    String devicetoken,
    String userid,
    String accesstoken,
    String getfollowersuserid) throws Exception {

    this.addParam(
      "device_token",
      devicetoken);

    this.addParam(
      "user_id",
      userid);

    this.addParam(
      "access_token",
      accesstoken);

    this.addParam(
      "get_followers_user_id",
      getfollowersuserid);

}

  /**
   * Constructor - RequestGetFollowers
   * this is the constructor for all request's params (mandatory and optional)
   * @param devicetoken
   * @param userid
   * @param accesstoken
   * @param getfollowersuserid
   * @param requesttrackingid
   * @param countlimit
   * @param atorbeforetime
   * @return new RequestGetFollowers Object
   * @throws Exception
   */
  public RequestGetFollowers (
    String devicetoken,
    String userid,
    String accesstoken,
    String getfollowersuserid,
    String requesttrackingid,
    int countlimit,
    long atorbeforetime) throws Exception {

    this.addParam(
      "device_token",
      devicetoken);

    this.addParam(
      "user_id",
      userid);

    this.addParam(
      "access_token",
      accesstoken);

    this.addParam(
      "get_followers_user_id",
      getfollowersuserid);

    this.addParam(
      "request_tracking_id",
      requesttrackingid);

    this.addParam(
      "count_limit",
      countlimit);

    this.addParam(
      "at_or_before_time",
      atorbeforetime);

}

  /*
   * Following are individual setters per-optional-param
   * */

  /**
   * setRequestTrackingId
   * @param requesttrackingid
   * @throws Exception
   */
  public void setRequestTrackingId (
    String requesttrackingid) throws Exception {

    this.addParam(
      "request_tracking_id",
      requesttrackingid);
  }

  /**
   * setCountLimit
   * @param countlimit
   * @throws Exception
   */
  public void setCountLimit (
    int countlimit) throws Exception {

    this.addParam(
      "count_limit",
      countlimit);
  }

  /**
   * setAtOrBeforeTime
   * @param atorbeforetime
   * @throws Exception
   */
  public void setAtOrBeforeTime (
    long atorbeforetime) throws Exception {

    this.addParam(
      "at_or_before_time",
      atorbeforetime);
  }
}
