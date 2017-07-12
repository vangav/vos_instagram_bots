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

package com.vangav.vos_instagram_bots.clients.vos_instagram.post_photo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vangav.backend.networks.rest_client.RestRequestPostJson;

/**
 * Generated using ClientsGeneratorInl
 */
/**
 * RequestPostPhoto represents the request to
 *   be sent to PostPhoto controller
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestPostPhoto extends RestRequestPostJson {

  /**
   * Constructor - RequestPostPhoto
   * this is the constructor for mandatory request's params only
   * @param devicetoken
   * @param userid
   * @param accesstoken
   * @param photo
   * @param photothumbnail
   * @return new RequestPostPhoto Object
   * @throws Exception
   */
  @JsonIgnore
  public RequestPostPhoto (
    String devicetoken,
    String userid,
    String accesstoken,
    String photo,
    String photothumbnail) throws Exception {

    this.device_token =
      devicetoken;
    this.user_id =
      userid;
    this.access_token =
      accesstoken;
    this.photo =
      photo;
    this.photo_thumbnail =
      photothumbnail;
}

  /**
   * Constructor - RequestPostPhoto
   * this is the constructor for all request's params (mandatory and optional)
   * @param devicetoken
   * @param userid
   * @param accesstoken
   * @param photo
   * @param photothumbnail
   * @param requesttrackingid
   * @param caption
   * @param latitude
   * @param longitude
   * @return new RequestPostPhoto Object
   * @throws Exception
   */
  @JsonIgnore
  public RequestPostPhoto (
    String devicetoken,
    String userid,
    String accesstoken,
    String photo,
    String photothumbnail,
    String requesttrackingid,
    String caption,
    double latitude,
    double longitude) throws Exception {

    this.device_token =
      devicetoken;
    this.user_id =
      userid;
    this.access_token =
      accesstoken;
    this.photo =
      photo;
    this.photo_thumbnail =
      photothumbnail;
    this.request_tracking_id =
      requesttrackingid;
    this.caption =
      caption;
    this.latitude =
      latitude;
    this.longitude =
      longitude;
}

  @Override
  @JsonIgnore
  protected String getName() throws Exception {

    return "post_photo";
  }

  @Override
  @JsonIgnore
  protected RequestPostPhoto getThis() throws Exception {

    return this;
  }

  @JsonProperty
  public String device_token;
  @JsonProperty
  public String user_id;
  @JsonProperty
  public String access_token;
  @JsonProperty
  public String photo;
  @JsonProperty
  public String photo_thumbnail;
  @JsonProperty
  public String request_tracking_id;
  @JsonProperty
  public String caption;
  @JsonProperty
  public double latitude;
  @JsonProperty
  public double longitude;

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
   * setCaption
   * @param caption
   * @throws Exception
   */
  @JsonIgnore
  public void setCaption (
    String caption) throws Exception {

    this.caption = caption;
  }

  /**
   * setLatitude
   * @param latitude
   * @throws Exception
   */
  @JsonIgnore
  public void setLatitude (
    double latitude) throws Exception {

    this.latitude = latitude;
  }

  /**
   * setLongitude
   * @param longitude
   * @throws Exception
   */
  @JsonIgnore
  public void setLongitude (
    double longitude) throws Exception {

    this.longitude = longitude;
  }
}
