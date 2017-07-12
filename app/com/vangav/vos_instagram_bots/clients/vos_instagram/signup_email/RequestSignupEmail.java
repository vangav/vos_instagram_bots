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

package com.vangav.vos_instagram_bots.clients.vos_instagram.signup_email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vangav.backend.networks.rest_client.RestRequestPostJson;

/**
 * Generated using ClientsGeneratorInl
 */
/**
 * RequestSignupEmail represents the request to
 *   be sent to SignupEmail controller
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestSignupEmail extends RestRequestPostJson {

  /**
   * Constructor - RequestSignupEmail
   * this is the constructor for mandatory request's params only
   * @param devicetoken
   * @param email
   * @param password
   * @param name
   * @return new RequestSignupEmail Object
   * @throws Exception
   */
  @JsonIgnore
  public RequestSignupEmail (
    String devicetoken,
    String email,
    String password,
    String name) throws Exception {

    this.device_token =
      devicetoken;
    this.email =
      email;
    this.password =
      password;
    this.name =
      name;
}

  /**
   * Constructor - RequestSignupEmail
   * this is the constructor for all request's params (mandatory and optional)
   * @param devicetoken
   * @param email
   * @param password
   * @param name
   * @param requesttrackingid
   * @param profilepicture
   * @return new RequestSignupEmail Object
   * @throws Exception
   */
  @JsonIgnore
  public RequestSignupEmail (
    String devicetoken,
    String email,
    String password,
    String name,
    String requesttrackingid,
    String profilepicture) throws Exception {

    this.device_token =
      devicetoken;
    this.email =
      email;
    this.password =
      password;
    this.name =
      name;
    this.request_tracking_id =
      requesttrackingid;
    this.profile_picture =
      profilepicture;
}

  @Override
  @JsonIgnore
  protected String getName() throws Exception {

    return "signup_email";
  }

  @Override
  @JsonIgnore
  protected RequestSignupEmail getThis() throws Exception {

    return this;
  }

  @JsonProperty
  public String device_token;
  @JsonProperty
  public String email;
  @JsonProperty
  public String password;
  @JsonProperty
  public String name;
  @JsonProperty
  public String request_tracking_id;
  @JsonProperty
  public String profile_picture;

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
   * setProfilePicture
   * @param profilepicture
   * @throws Exception
   */
  @JsonIgnore
  public void setProfilePicture (
    String profilepicture) throws Exception {

    this.profile_picture = profilepicture;
  }
}
