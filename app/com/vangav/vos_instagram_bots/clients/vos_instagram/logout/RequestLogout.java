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

package com.vangav.vos_instagram_bots.clients.vos_instagram.logout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vangav.backend.networks.rest_client.RestRequestPostJson;

/**
 * Generated using ClientsGeneratorInl
 */
/**
 * RequestLogout represents the request to
 *   be sent to Logout controller
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestLogout extends RestRequestPostJson {

  /**
   * Constructor - RequestLogout
   * this is the constructor for mandatory request's params only
   * @param devicetoken
   * @param userid
   * @param accesstoken
   * @param refreshtoken
   * @return new RequestLogout Object
   * @throws Exception
   */
  @JsonIgnore
  public RequestLogout (
    String devicetoken,
    String userid,
    String accesstoken,
    String refreshtoken) throws Exception {

    this.device_token =
      devicetoken;
    this.user_id =
      userid;
    this.access_token =
      accesstoken;
    this.refresh_token =
      refreshtoken;
}

  /**
   * Constructor - RequestLogout
   * this is the constructor for all request's params (mandatory and optional)
   * @param devicetoken
   * @param userid
   * @param accesstoken
   * @param refreshtoken
   * @param requesttrackingid
   * @param logouttype
   * @return new RequestLogout Object
   * @throws Exception
   */
  @JsonIgnore
  public RequestLogout (
    String devicetoken,
    String userid,
    String accesstoken,
    String refreshtoken,
    String requesttrackingid,
    String logouttype) throws Exception {

    this.device_token =
      devicetoken;
    this.user_id =
      userid;
    this.access_token =
      accesstoken;
    this.refresh_token =
      refreshtoken;
    this.request_tracking_id =
      requesttrackingid;
    this.logout_type =
      logouttype;
}

  @Override
  @JsonIgnore
  protected String getName() throws Exception {

    return "logout";
  }

  @Override
  @JsonIgnore
  protected RequestLogout getThis() throws Exception {

    return this;
  }

  @JsonProperty
  public String device_token;
  @JsonProperty
  public String user_id;
  @JsonProperty
  public String access_token;
  @JsonProperty
  public String refresh_token;
  @JsonProperty
  public String request_tracking_id;
  @JsonProperty
  public String logout_type;

  /*
   * Following are individual setters per-optional-param
   * */

  /**
   * setRequestTrackingId
   * @param requesttrackingid
   * @throws Exception
   */
  @JsonIgnore
  public void setRequestTrackingId (
    String requesttrackingid) throws Exception {

    this.request_tracking_id = requesttrackingid;
  }

  /**
   * setLogoutType
   * @param logouttype
   * @throws Exception
   */
  @JsonIgnore
  public void setLogoutType (
    String logouttype) throws Exception {

    this.logout_type = logouttype;
  }
}
