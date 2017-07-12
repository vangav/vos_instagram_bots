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

package com.vangav.vos_instagram_bots.bots;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.ResultSet;
import com.vangav.backend.backend_client_java.BackendClientSession;
import com.vangav.backend.backend_client_java.BackendClientSession.RestCallsType;
import com.vangav.backend.backend_client_java.ControllerCall;
import com.vangav.backend.backend_client_java.ControllerCallLog;
import com.vangav.backend.cassandra.formatting.CalendarFormatterInl;
import com.vangav.backend.content.generation.RandomGeneratorInl;
import com.vangav.backend.metrics.time.CalendarAndDateOperationsInl;
import com.vangav.backend.metrics.time.Period;
import com.vangav.backend.metrics.time.TimeUnitType;
import com.vangav.backend.thread_pool.periodic_jobs.CycleTicker.CycleTickerBuilder;
import com.vangav.backend.thread_pool.periodic_jobs.CycleLog;
import com.vangav.backend.thread_pool.periodic_jobs.PeriodicJob;
import com.vangav.vos_instagram_bots.cassandra_keyspaces.ig_bots.PostsCount;
import com.vangav.vos_instagram_bots.cassandra_keyspaces.ig_bots.PostsIndex;
import com.vangav.vos_instagram_bots.cassandra_keyspaces.ig_bots.UsersCount;
import com.vangav.vos_instagram_bots.cassandra_keyspaces.ig_bots.UsersIndex;
import com.vangav.vos_instagram_bots.clients.vos_instagram.comment.ControllerCallComment;
import com.vangav.vos_instagram_bots.clients.vos_instagram.comment.RequestComment;
import com.vangav.vos_instagram_bots.clients.vos_instagram.follow.ControllerCallFollow;
import com.vangav.vos_instagram_bots.clients.vos_instagram.follow.RequestFollow;
import com.vangav.vos_instagram_bots.clients.vos_instagram.like_post.ControllerCallLikePost;
import com.vangav.vos_instagram_bots.clients.vos_instagram.like_post.RequestLikePost;
import com.vangav.vos_instagram_bots.clients.vos_instagram.post_photo.ControllerCallPostPhoto;
import com.vangav.vos_instagram_bots.clients.vos_instagram.post_photo.RequestPostPhoto;
import com.vangav.vos_instagram_bots.clients.vos_instagram.post_photo.ResponsePostPhoto;
import com.vangav.vos_instagram_bots.clients.vos_instagram.signup_email.ControllerCallSignupEmail;
import com.vangav.vos_instagram_bots.clients.vos_instagram.signup_email.RequestSignupEmail;
import com.vangav.vos_instagram_bots.clients.vos_instagram.signup_email.ResponseSignupEmail;
import com.vangav.vos_instagram_bots.clients.vos_instagram.update_location.ControllerCallUpdateLocation;
import com.vangav.vos_instagram_bots.clients.vos_instagram.update_location.RequestUpdateLocation;

/**
 * @author mustapha
 * fb.com/mustapha.abdallah
 */
/**
 * CityBot is a periodic job that runs async daily per bot-city + bot-stars
 * The quantity of activity coming from a city is directly proportional to the
 *   population of that city
 * Each city has some stars keyed using *stars_airport_code*; also directly
 *   proportional to the population of that city
 * Each period the job executes the following sequence:
 * 1- Create a random number of users (generally more users every week)
 * 2- Some of the users created today post a photo
 * 3- Some of the users created on that day start following users randomly from
 *      the past
 *        - follow numbers generally increase every week
 *        - get followed back by more of the non stars
 *        - more users from their city than other cities
 * 4- Some of the users created in the past in this city post a photo
 * 5- Some of the users created today interact with few posts from the past
 *      week that were posted until today in their city and until yesterday
 *      from other cities
 *        - more posts from their city than other cities
 * 6- Some of the users created in the past interact with posts from today in
 *      their city and from yesterday in other cities
 *        - more posts from their city than other cities
 */
public class CityBot extends PeriodicJob<CityBot> {
  
  private Calendar startCalendar;
  private String airportCode;
  private double latitude;
  private double longitude;
  private int populationInMillions;
  private String addedYearMonthDay;
  private Calendar addedDate;
  private Calendar currentCalendar;
  private Calendar yesterdayCalendar;
  private Calendar lastWeekCalendar;
  private ArrayList<Calendar> startTillYesterdayCalendars;
  private ArrayList<Calendar> lastWeekTillYesterdayCalendars;
  private ArrayList<Calendar> lastWeekTillTodayCalendars;
  private int weekCount;
  private BackendClientSession backendClientSession;

  /**
   * Constructor - CityBot
   * @param startCalendar
   * @param airportCode
   * @param latitude
   * @param longitude
   * @param populationInMillions
   * @param addedYearMonthDay
   * @return new CityBot Object
   * @throws Exception
   */
  public CityBot (
    Calendar startCalendar,
    String airportCode,
    double latitude,
    double longitude,
    int populationInMillions,
    String addedYearMonthDay) throws Exception {
    
    super(
      "city_bot",
      PeriodicJob.Type.ASYNC,
      startCalendar,
      new Period(
        1.0,
        TimeUnitType.DAY),
      new CycleTickerBuilder()
        .runFor(new Period(1, TimeUnitType.YEAR) )
        .build() );
    
    this.startCalendar = (Calendar)startCalendar.clone();
    
    this.airportCode = airportCode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.populationInMillions = populationInMillions;
    
    this.addedYearMonthDay = addedYearMonthDay;
    this.addedDate = Calendar.getInstance();
    
    String[] addedDateParts = addedYearMonthDay.split("_");
    
    this.addedDate.set(
      Integer.parseInt(addedDateParts[0] ),
      Integer.parseInt(addedDateParts[1] ),
      Integer.parseInt(addedDateParts[2] ) );
    
    this.currentCalendar = CalendarAndDateOperationsInl.getCurrentCalendar();
    this.yesterdayCalendar = (Calendar)this.currentCalendar.clone();
    this.yesterdayCalendar.add(Calendar.DATE, -1);
    this.lastWeekCalendar = (Calendar)this.currentCalendar.clone();
    this.lastWeekCalendar.add(Calendar.DATE, -6);
    this.startTillYesterdayCalendars =
      CalendarAndDateOperationsInl.getCalendarsFromTo(
        this.startCalendar,
        this.yesterdayCalendar);
    this.lastWeekTillYesterdayCalendars =
      CalendarAndDateOperationsInl.getCalendarsFromTo(
        this.lastWeekCalendar,
        this.yesterdayCalendar);
    this.lastWeekTillTodayCalendars =
      CalendarAndDateOperationsInl.getCalendarsFromTo(
        this.lastWeekCalendar,
        this.currentCalendar);
    this.weekCount =
      CalendarAndDateOperationsInl.getWeekCalendarsRanges(
        this.startCalendar,
        this.currentCalendar).size();
  }
  
  @Override
  protected PeriodicJob<CityBot> deepCopy () throws Exception {
    
    return
      new CityBot(
        this.startCalendar,
        this.airportCode,
        this.latitude,
        this.longitude,
        this.populationInMillions,
        this.addedYearMonthDay);
  }

  @Override
  protected void process(CycleLog cycleLog) throws Exception {
    
    // reseting backend client session before each step to minimize the
    //   memory needed for controller-call-logs
    
    // 1- create users and star users
    
    this.backendClientSession =
      new BackendClientSession(
        "vos_instagram_bot_"
          + this.airportCode
          + "_"
          + this.weekCount
          + "_"
          + "create_users",
        true);
    
    Set<String> createdUsers = this.createUsers();

    this.backendClientSession =
      new BackendClientSession(
        "vos_instagram_bot_"
          + this.airportCode
          + "_"
          + this.weekCount
          + "_"
          + "create_star_users",
        true);
    
    Set<String> createdStarUsers = this.createStarUsers();
    
    // 2- some of the created users post photos

    this.backendClientSession =
      new BackendClientSession(
        "vos_instagram_bot_"
          + this.airportCode
          + "_"
          + this.weekCount
          + "_"
          + "post_photos_new_users",
        true);
    
    this.postPhotos(createdUsers, createdStarUsers);
    
    // 3- some of the created users follow other users and get followed by
    //      other users

    this.backendClientSession =
      new BackendClientSession(
        "vos_instagram_bot_"
          + this.airportCode
          + "_"
          + this.weekCount
          + "_"
          + "follow_users",
        true);
    
    this.followUsers(createdUsers, createdStarUsers);
    
    // 4- some of the old users post photos
    

    this.backendClientSession =
      new BackendClientSession(
        "vos_instagram_bot_"
          + this.airportCode
          + "_"
          + this.weekCount
          + "_"
          + "post_photos_old_users",
        true);
    
    this.postPhotos();
    
    // 5- created users interact with old posts

    this.backendClientSession =
      new BackendClientSession(
        "vos_instagram_bot_"
          + this.airportCode
          + "_"
          + this.weekCount
          + "_"
          + "interact_new_users",
        true);
    
    this.interactWithPosts(createdUsers);
    
    // 6- some of the old users interact with posts from yesterday

    this.backendClientSession =
      new BackendClientSession(
        "vos_instagram_bot_"
          + this.airportCode
          + "_"
          + this.weekCount
          + "_"
          + "interact_old_users",
        true);
    
    this.interactWithPosts();
  }
  
  private static final String kDeviceToken = "device-token";
  private static final String kEmailSuffix = "@vangav.com";
  private static final String kPassword = "password";
  private static final String kAccessToken = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6";
  private static final String kStarUserPrefix = "stars";
  private static final double kUserLocationRange = 0.2;
  private static final double kStarUserLocationRange = 0.1;
  private static final String kPhoto = "YQ==";
  private static final String kComment = "C";
  
  /**
   * 1- createUsers
   * creates new users for this city on this day
   * number of created users is decided using
   * - general pivot starts at 10 and is incremented by 10 each week
   * - to randomize the number of generated user, the number is picked within
   *     +/-5 of the pivot above (e.g.: 10 would be a random number
   *     between 5 and 15)
   * - then based on the city's population (1 - 19) 10 keeps the range as is
   *     while lower numbers bring the upper limit closer to the pivot and
   *     higher numbers bring the lower limit closer to the pivot
   *     e.g. on pivot = 10:
   *       - population 10 --> 5 - 15
   *       - population 1  --> 5 - 10
   *       - population 19 --> 10 - 15
   * @return created users' ids
   * @throws Exception
   */
  private Set<String> createUsers () throws Exception {
    
    // calculate count of new users
    
    final double pivotFactor = 10;
    
    double pivot = pivotFactor * this.weekCount;
    double minPivot = pivot - 5;
    double maxPivot = pivot + 5;
    
    if (this.populationInMillions > 10) {
      
      minPivot += ((this.populationInMillions - 10) / 2);
      minPivot = Math.min(minPivot, pivot);
    } else if (this.populationInMillions < 10) {
      
      maxPivot -= (this.populationInMillions / 2);
      maxPivot = Math.max(maxPivot, pivot);
    }
    
    int usersCount =
      (int)RandomGeneratorInl.generateRandomDouble(minPivot, maxPivot);
    
    // create new users
    
    Set<String> createdUsersIds = new HashSet<String>();
    
    ControllerCallLog currControllerCallLog;
    String currName;
    
    ResponseSignupEmail currResponse;
    String currUserUuidStr;
    UUID currUserUuid;
    
    for (int i = 1; i <= usersCount; i ++) {
      
      currName =
        this.airportCode
        + "_"
        + this.weekCount
        + "_"
        + i;
      
      // send sign up request
      currControllerCallLog =
        this.backendClientSession.executeControllersCalls(
          RestCallsType.SYNC,
          new ControllerCallSignupEmail(
            new RequestSignupEmail(
              kDeviceToken,
              currName + kEmailSuffix,
              kPassword,
              currName) ) ).get(0);
      
      // fail?
      if (currControllerCallLog.getResponseHttpStatusCode() !=
            HttpURLConnection.HTTP_OK) {
        
        i -= 1;
        continue;
      }
      
      // extract user's uuid
      
      currResponse = (ResponseSignupEmail)currControllerCallLog.getResponse();
      
      currUserUuidStr = currResponse.user_id;
      currUserUuid = UUID.fromString(currUserUuidStr);
      
      // store it in the DB
      UsersIndex.i().executeSyncInsert(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH),
        this.airportCode,
        i,
        currUserUuid,
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH) );
      
      // store in result
      createdUsersIds.add(currUserUuidStr);
      
      // update user's location
      this.backendClientSession.executeControllersCalls(
        RestCallsType.SYNC,
        new ControllerCallUpdateLocation(
          new RequestUpdateLocation(
            kDeviceToken,
            currUserUuidStr,
            kAccessToken,
            RandomGeneratorInl.generateRandomDouble(
              this.latitude - kUserLocationRange,
              this.latitude + kUserLocationRange),
            RandomGeneratorInl.generateRandomDouble(
              this.longitude - kUserLocationRange,
              this.longitude + kUserLocationRange) ) ) );
    }
    
    // update user's count table
    UsersCount.i().executeSyncIncrementValue(
      usersCount,
      CalendarFormatterInl.concatCalendarFields(
        this.currentCalendar,
        Calendar.YEAR,
        Calendar.MONTH,
        Calendar.DAY_OF_MONTH)
        + "_"
        + this.airportCode);
    
    return createdUsersIds;
  }
  
  /**
   * 1- createStarUsers
   * creates new star users for this city on this day
   * number of created users is decided using
   * - general pivot starts at 1 and is incremented by 1 each week
   * - to randomize the number of generated user, the number is picked within
   *     +/-2 of the pivot above (e.g.: 10 would be a random number
   *     between 8 and 12)
   * - then based on the city's population (1 - 19) 10 keeps the range as is
   *     while lower numbers bring the upper limit closer to the pivot and
   *     higher numbers bring the lower limit closer to the pivot
   *     e.g. on pivot = 10:
   *       - population 10 --> 8 - 12
   *       - population 1  --> 8 - 10
   *       - population 19 --> 10 - 12
   * @return created star user's ids
   * @throws Exception
   */
  private Set<String> createStarUsers () throws Exception {
    
    // calculate count of new users
    
    final double pivotFactor = 1;
    
    double pivot = pivotFactor * this.weekCount;
    double minPivot = pivot - 2;
    double maxPivot = pivot + 2;
    
    if (this.populationInMillions > 10) {
      
      minPivot += ((this.populationInMillions - 10) / 2);
      minPivot = Math.min(minPivot, pivot);
    } else if (this.populationInMillions < 10) {
      
      maxPivot -= (this.populationInMillions / 2);
      maxPivot = Math.max(maxPivot, pivot);
    }
    
    int usersCount =
      (int)RandomGeneratorInl.generateRandomDouble(minPivot, maxPivot);
    
    // adjust users count
    if (usersCount < 0) {
      
      usersCount = 0;
    }
    
    // create new users
    
    Set<String> createdStarUsersIds = new HashSet<String>();
    
    ControllerCallLog currControllerCallLog;
    String currName;
    
    ResponseSignupEmail currResponse;
    String currStarUserUuidStr;
    UUID currStarUserUuid;
    
    for (int i = 1; i <= usersCount; i ++) {
      
      currName =
        kStarUserPrefix
        + "_"
        + this.airportCode
        + "_"
        + this.weekCount
        + "_"
        + i;
      
      // send sign up request
      currControllerCallLog =
        this.backendClientSession.executeControllersCalls(
          RestCallsType.SYNC,
          new ControllerCallSignupEmail(
            new RequestSignupEmail(
              kDeviceToken,
              currName + kEmailSuffix,
              kPassword,
              currName) ) ).get(0);
      
      // fail?
      if (currControllerCallLog.getResponseHttpStatusCode() !=
            HttpURLConnection.HTTP_OK) {
        
        i -= 1;
        continue;
      }
      
      // extract user's uuid
      
      currResponse = (ResponseSignupEmail)currControllerCallLog.getResponse();
      
      currStarUserUuidStr = currResponse.user_id;
      currStarUserUuid = UUID.fromString(currStarUserUuidStr);
      
      // store it in the DB
      UsersIndex.i().executeSyncInsert(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH),
        kStarUserPrefix
          + "_"
          + this.airportCode,
        i,
        currStarUserUuid,
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH) );
      
      // store in result
      createdStarUsersIds.add(currStarUserUuidStr);
      
      // update user's location
      this.backendClientSession.executeControllersCalls(
        RestCallsType.SYNC,
        new ControllerCallUpdateLocation(
          new RequestUpdateLocation(
            kDeviceToken,
            currStarUserUuidStr,
            kAccessToken,
            RandomGeneratorInl.generateRandomDouble(
              this.latitude - kStarUserLocationRange,
              this.latitude + kStarUserLocationRange),
            RandomGeneratorInl.generateRandomDouble(
              this.longitude - kStarUserLocationRange,
              this.longitude + kStarUserLocationRange) ) ) );
    }
    
    // update user's count table
    UsersCount.i().executeSyncIncrementValue(
      usersCount,
      CalendarFormatterInl.concatCalendarFields(
        this.currentCalendar,
        Calendar.YEAR,
        Calendar.MONTH,
        Calendar.DAY_OF_MONTH)
        + "_"
        + kStarUserPrefix
        + "_"
        + this.airportCode);
    
    return createdStarUsersIds;
  }
  
  /**
   * 2- postPhotos
   * some of the created users and star users post photos
   *   1- 70% of the normal users post a photo on the first day
   *   2- 90% of the star users post a photo on the first day
   * @param createdUsers
   * @param createdStarUsers
   * @throws Exception
   */
  private void postPhotos (
    Set<String> createdUsers,
    Set<String> createdStarUsers) throws Exception {
    
    int currRandom;
    ControllerCallLog currControllerCallLog;
    ResponsePostPhoto currResponse;
    UUID currPostId;
    int seqId = 1;
    
    // 1- 70% of the normal users post a photo on the first day
    for (String userId : createdUsers) {
      
      currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
      
      // 30% of the users don't post a photo on their first day
      if (currRandom <= 3) {
        
        continue;
      }
      
      // send post photo request
      currControllerCallLog =
        this.backendClientSession.executeControllersCalls(
          RestCallsType.SYNC,
          new ControllerCallPostPhoto(
            new RequestPostPhoto(
              kDeviceToken,
              userId,
              kAccessToken,
              kPhoto,
              kPhoto,
              "",
              "",
              RandomGeneratorInl.generateRandomDouble(
                this.latitude - kUserLocationRange,
                this.latitude + kUserLocationRange),
              RandomGeneratorInl.generateRandomDouble(
                this.longitude - kUserLocationRange,
                this.longitude + kUserLocationRange) ) ) ).get(0);
      
      // fail?
      if (currControllerCallLog.getResponseHttpStatusCode() !=
            HttpURLConnection.HTTP_OK) {
        
        continue;
      }
      
      // extract post's id
      
      currResponse = (ResponsePostPhoto)currControllerCallLog.getResponse();
      currPostId = UUID.fromString(currResponse.post_id);
      
      // store post in posts index
      PostsIndex.i().executeSyncInsert(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH),
        this.airportCode,
        seqId,
        currPostId);
      
      seqId += 1;
      
      // increment posts count
      PostsCount.i().executeSyncIncrement(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH)
        + "_"
        + this.airportCode);
    }
    
    // reset sequence id
    seqId = 1;
    
    // 2- 90% of the star users post a photo on the first day
    for (String userId : createdStarUsers) {
      
      currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
      
      // 10% of the users don't post a photo on their first day
      if (currRandom <= 1) {
        
        continue;
      }
      
      // send post photo request
      currControllerCallLog =
        this.backendClientSession.executeControllersCalls(
          RestCallsType.SYNC,
          new ControllerCallPostPhoto(
            new RequestPostPhoto(
              kDeviceToken,
              userId,
              kAccessToken,
              kPhoto,
              kPhoto,
              "",
              "",
              RandomGeneratorInl.generateRandomDouble(
                this.latitude - kStarUserLocationRange,
                this.latitude + kStarUserLocationRange),
              RandomGeneratorInl.generateRandomDouble(
                this.longitude - kStarUserLocationRange,
                this.longitude + kStarUserLocationRange) ) ) ).get(0);
      
      // fail?
      if (currControllerCallLog.getResponseHttpStatusCode() !=
            HttpURLConnection.HTTP_OK) {
        
        continue;
      }
      
      // extract post's id
      
      currResponse = (ResponsePostPhoto)currControllerCallLog.getResponse();
      currPostId = UUID.fromString(currResponse.post_id);
      
      // store post in posts index
      PostsIndex.i().executeSyncInsert(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH),
        kStarUserPrefix
          + "_"
          + this.airportCode,
        seqId,
        currPostId);
      
      seqId += 1;
      
      // increment posts count
      PostsCount.i().executeSyncIncrement(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH)
        + "_"
        + kStarUserPrefix
        + "_"
        + this.airportCode);
    }
  }
  
  /**
   * 3- followUsers
   * some of the created users and star users follow other users and get
   *   followed by some of those followed users
   * users:
   *   1- follow 10% of the users in the their city (50% follow back)
   *   2- follow 50% of the star users in their city (1% follow back)
   *   3- follow 1% of the users in 30% of the other cities (50% follow back)
   *   4- follow 20% of the star users in 50% of the other cities
   *        (1% follow back)
   * star users:
   *   5- follow 50% of the star users in their city (50% follow back)
   *   6- follow 10% of the star users in 50% of the other cities
   *        (50% follow back)
   * @param createdUsers
   * @param createdStarUsers
   * @throws Exception
   */
  private void followUsers (
    Set<String> createdUsers,
    Set<String> createdStarUsers) throws Exception {
    
    // current random is used to get probabilities (10%, 30%, ...)
    int currRandom;
    ResultSet resultSet;
    int currUsersCount;
    String currSelectedUserId;
    
    // users:
    
    // 1- users follow 10% of the users in their city (50% follow back)
    for (String userId : createdUsers) {
      
      // for each day since the start of this service till yesterday
      for (Calendar calendar : this.startTillYesterdayCalendars) {
        
        // select the count of users created on that day in this city
        resultSet =
          UsersCount.i().executeSyncSelect(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH)
              + "_"
              + this.airportCode);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract the count of users created on that day in this city
        currUsersCount =
          (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
        
        // follow users
        for (int i = 1; i <= currUsersCount; i ++) {
          
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
          
          // follow 10% only
          if (currRandom != 1) {
            
            continue;
          }
          
          // select user's id
          resultSet =
            UsersIndex.i().executeSyncSelectUser(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH),
              this.airportCode,
              i);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract user's id
          currSelectedUserId =
            resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
          
          // send follow user request
          this.backendClientSession.executeControllersCalls(
            RestCallsType.SYNC,
            new ControllerCallFollow(
              new RequestFollow(
                kDeviceToken,
                userId,
                kAccessToken,
                currSelectedUserId) ) );
          
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 2);
          
          // 50% follow back
          if (currRandom == 1) {
            
            // send follow back request
            this.backendClientSession.executeControllersCalls(
              RestCallsType.SYNC,
              new ControllerCallFollow(
                new RequestFollow(
                  kDeviceToken,
                  currSelectedUserId,
                  kAccessToken,
                  userId) ) );
          }
        } // for (int i = 1; i <= currUsersCount; i ++) {
      } // for (Calendar calendar : this.startTillYesterdayCalendars) {
    } // for (String userId : createdUsers) {
    
    // 2- follow 50% of the star users in their city (1% follow back)
    for (String userId : createdUsers) {
      
      // for each day since the start of this service till yesterday
      for (Calendar calendar : this.startTillYesterdayCalendars) {
        
        // select the count of star users created on that day in this city
        resultSet =
          UsersCount.i().executeSyncSelect(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH)
              + "_"
              + kStarUserPrefix
              + "_"
              + this.airportCode);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract the count of star users created on that day in this city
        currUsersCount =
          (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
        
        // follow users
        for (int i = 1; i <= currUsersCount; i ++) {
          
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 2);
          
          // follow 50% only
          if (currRandom != 1) {
            
            continue;
          }
          
          // select user's id
          resultSet =
            UsersIndex.i().executeSyncSelectUser(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH),
              kStarUserPrefix
                + "_"
                + this.airportCode,
              i);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract user's id
          currSelectedUserId =
            resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
          
          // send follow user request
          this.backendClientSession.executeControllersCalls(
            RestCallsType.SYNC,
            new ControllerCallFollow(
              new RequestFollow(
                kDeviceToken,
                userId,
                kAccessToken,
                currSelectedUserId) ) );
          
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
          
          // 1% follow back
          if (currRandom == 1) {
            
            // send follow back request
            this.backendClientSession.executeControllersCalls(
              RestCallsType.SYNC,
              new ControllerCallFollow(
                new RequestFollow(
                  kDeviceToken,
                  currSelectedUserId,
                  kAccessToken,
                  userId) ) );
          }
        } // for (int i = 1; i <= currUsersCount; i ++) {
      } // for (Calendar calendar : this.startTillYesterdayCalendars) {
    } // for (String userId : createdUsers) {
    
    // 3- follow 1% of the users in 30% of the other cities (50% follow back)
    for (String userId : createdUsers) {
      
      // for each other city
      for (String airportCode : CityBotsLoader.getAirportCodes() ) {
        
        // doing other cities only
        if (airportCode.compareTo(this.airportCode) == 0) {
          
          continue;
        }
        
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
        
        // 30% of the cities only
        if (currRandom > 3) {
          
          continue;
        }
        
        // for each day since the start of this service till yesterday
        for (Calendar calendar : this.startTillYesterdayCalendars) {
          
          // select the count of users created on that day in this city
          resultSet =
            UsersCount.i().executeSyncSelect(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH)
                + "_"
                + airportCode);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract the count of users created on that day in this city
          currUsersCount =
            (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
          
          // follow users
          for (int i = 1; i <= currUsersCount; i ++) {
            
            currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
            
            // follow 1% inly
            if (currRandom != 1) {
              
              continue;
            }
            
            // select user's id
            resultSet =
              UsersIndex.i().executeSyncSelectUser(
                CalendarFormatterInl.concatCalendarFields(
                  calendar,
                  Calendar.YEAR,
                  Calendar.MONTH,
                  Calendar.DAY_OF_MONTH),
                airportCode,
                i);
            
            if (resultSet.isExhausted() == true) {
              
              continue;
            }
            
            // extract user's id
            currSelectedUserId =
              resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
            
            // send follow user request
            this.backendClientSession.executeControllersCalls(
              RestCallsType.SYNC,
              new ControllerCallFollow(
                new RequestFollow(
                  kDeviceToken,
                  userId,
                  kAccessToken,
                  currSelectedUserId) ) );
            
            currRandom = RandomGeneratorInl.generateRandomInteger(1, 2);
            
            // 50% follow back
            if (currRandom == 1) {
              
              // send follow back request
              this.backendClientSession.executeControllersCalls(
                RestCallsType.SYNC,
                new ControllerCallFollow(
                  new RequestFollow(
                    kDeviceToken,
                    currSelectedUserId,
                    kAccessToken,
                    userId) ) );
            }
          } // for (int i = 1; i <= currUsersCount; i ++) {
        } // for (Calendar calendar : this.startTillYesterdayCalendars) {
      } // for (String airportCode : CityBotsLoader.getAirportCodes() ) {
    } // for (String userId : createdUsers) {
    
    // 4- follow 20% of the star users in 50% of the other cities
    //      (1% follow back)
    for (String userId : createdUsers) {
      
      // for each other city
      for (String airportCode : CityBotsLoader.getAirportCodes() ) {
        
        // doing other cities only
        if (airportCode.compareTo(this.airportCode) == 0) {
          
          continue;
        }
        
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
        
        // 50% of the cities only
        if (currRandom > 5) {
          
          continue;
        }
        
        // for each day since the start of this service till yesterday
        for (Calendar calendar : this.startTillYesterdayCalendars) {
          
          // select the count of star users created on that day in this city
          resultSet =
            UsersCount.i().executeSyncSelect(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH)
                + "_"
                + kStarUserPrefix
                + "_"
                + airportCode);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract the count of star users created on that day in this city
          currUsersCount =
            (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
          
          // follow users
          for (int i = 1; i <= currUsersCount; i ++) {
            
            currRandom = RandomGeneratorInl.generateRandomInteger(1, 5);
            
            // follow 20% only
            if (currRandom != 1) {
              
              continue;
            }
            
            // select user's id
            resultSet =
              UsersIndex.i().executeSyncSelectUser(
                CalendarFormatterInl.concatCalendarFields(
                  calendar,
                  Calendar.YEAR,
                  Calendar.MONTH,
                  Calendar.DAY_OF_MONTH),
                kStarUserPrefix
                  + "_"
                  + airportCode,
                i);
            
            if (resultSet.isExhausted() == true) {
              
              continue;
            }
            
            // extract user's id
            currSelectedUserId =
              resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
            
            // send follow user request
            this.backendClientSession.executeControllersCalls(
              RestCallsType.SYNC,
              new ControllerCallFollow(
                new RequestFollow(
                  kDeviceToken,
                  userId,
                  kAccessToken,
                  currSelectedUserId) ) );
            
            currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
            
            // 1% follow back
            if (currRandom == 1) {
              
              // send follow back request
              this.backendClientSession.executeControllersCalls(
                RestCallsType.SYNC,
                new ControllerCallFollow(
                  new RequestFollow(
                    kDeviceToken,
                    currSelectedUserId,
                    kAccessToken,
                    userId) ) );
            }
          } // for (int i = 1; i <= currUsersCount; i ++) {
        } // for (Calendar calendar : this.startTillYesterdayCalendars) {
      } // for (String airportCode : CityBotsLoader.getAirportCodes() ) {
    } // for (String userId : createdUsers) {
    
    // star users:
    
    // 5- follow 50% of the star users in their city (50% follow back)
    for (String userId : createdStarUsers) {
      
      // for each day since the start of this service till yesterday
      for (Calendar calendar : this.startTillYesterdayCalendars) {
        
        // select the count of star users created on that day in this city
        resultSet =
          UsersCount.i().executeSyncSelect(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH)
              + "_"
              + kStarUserPrefix
              + "_"
              + this.airportCode);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract the count of star users created on that day in this city
        currUsersCount =
          (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
        
        // follow users
        for (int i = 1; i <= currUsersCount; i ++) {
          
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 2);
          
          // follow 50% only
          if (currRandom != 1) {
            
            continue;
          }
          
          // select user's id
          resultSet =
            UsersIndex.i().executeSyncSelectUser(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH),
              kStarUserPrefix
                + "_"
                + this.airportCode,
              i);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract user's id
          currSelectedUserId =
            resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
          
          // send follow user request
          this.backendClientSession.executeControllersCalls(
            RestCallsType.SYNC,
            new ControllerCallFollow(
              new RequestFollow(
                kDeviceToken,
                userId,
                kAccessToken,
                currSelectedUserId) ) );
          
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 2);
          
          // 50% follow back
          if (currRandom == 1) {
            
            // send follow back request
            this.backendClientSession.executeControllersCalls(
              RestCallsType.SYNC,
              new ControllerCallFollow(
                new RequestFollow(
                  kDeviceToken,
                  currSelectedUserId,
                  kAccessToken,
                  userId) ) );
          }
        } // for (int i = 1; i <= currUsersCount; i ++) {
      } // for (Calendar calendar : this.startTillYesterdayCalendars) {
    } // for (String userId : createdStarUsers) {
    
    // 6- follow 10% of the star users in 50% of the other cities
    //      (50% follow back)
    for (String userId : createdStarUsers) {
      
      // for each other city
      for (String airportCode : CityBotsLoader.getAirportCodes() ) {
        
        // doing other cities only
        if (airportCode.compareTo(this.airportCode) == 0) {
          
          continue;
        }
        
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
        
        // 50% of the cities only
        if (currRandom > 5) {
          
          continue;
        }
        
        // for each day since the start of this service till yesterday
        for (Calendar calendar : this.startTillYesterdayCalendars) {
          
          // select the count of star users created on that day in this city
          resultSet =
            UsersCount.i().executeSyncSelect(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH)
                + "_"
                + kStarUserPrefix
                + "_"
                + airportCode);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract the count of star users created on that day in this city
          currUsersCount =
            (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
          
          // follow users
          for (int i = 1; i <= currUsersCount; i ++) {
            
            currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
            
            // follow 10% only
            if (currRandom != 1) {
              
              continue;
            }
            
            // select user's id
            resultSet =
              UsersIndex.i().executeSyncSelectUser(
                CalendarFormatterInl.concatCalendarFields(
                  calendar,
                  Calendar.YEAR,
                  Calendar.MONTH,
                  Calendar.DAY_OF_MONTH),
                kStarUserPrefix
                  + "_"
                  + airportCode,
                i);
            
            if (resultSet.isExhausted() == true) {
              
              continue;
            }
            
            // extract user's id
            currSelectedUserId =
              resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
            
            // send follow user request
            this.backendClientSession.executeControllersCalls(
              RestCallsType.SYNC,
              new ControllerCallFollow(
                new RequestFollow(
                  kDeviceToken,
                  userId,
                  kAccessToken,
                  currSelectedUserId) ) );
            
            currRandom = RandomGeneratorInl.generateRandomInteger(1, 2);
            
            // 50% follow back
            if (currRandom == 1) {
              
              // send follow back request
              this.backendClientSession.executeControllersCalls(
                RestCallsType.SYNC,
                new ControllerCallFollow(
                  new RequestFollow(
                    kDeviceToken,
                    currSelectedUserId,
                    kAccessToken,
                    userId) ) );
            }
          } // for (int i = 1; i <= currUsersCount; i ++) {
        } // for (Calendar calendar : this.startTillYesterdayCalendars) {
      } // for (String airportCode : CityBotsLoader.getAirportCodes() ) {
    } // for (String userId : createdStarUsers) {
  }
  
  /**
   * 4- postPhotos
   *      some of the users from the past post photos
   * 
   *   1- 25% of the users created in the past post a photo
   *   2- 60% of the star users created in the past post a photo
   * @throws Exception
   */
  private void postPhotos () throws Exception {
    
    // current random is used to get probabilities (10%, 30%, ...)
    int currRandom;
    ResultSet resultSet;
    int currUsersCount;
    String currSelectedUserId;
    ControllerCallLog currControllerCallLog;
    ResponsePostPhoto currResponse;
    UUID currPostId;
    int seqId = 1;
    
    // 1- 25% of the users created in the past post a photo
    
    // for each day since the start of this service till yesterday
    for (Calendar calendar : this.startTillYesterdayCalendars) {
      
      // select the count of users created on that day in this city
      resultSet =
        UsersCount.i().executeSyncSelect(
          CalendarFormatterInl.concatCalendarFields(
            calendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
            + "_"
            + this.airportCode);
      
      if (resultSet.isExhausted() == true) {
        
        continue;
      }
      
      // extract the count of users created on that day in this city
      currUsersCount =
        (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
      
      // post photos
      for (int i = 1; i <= currUsersCount; i ++) {
        
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 4);
        
        // 25% of the users only post a new photo
        if (currRandom != 1) {
          
          continue;
        }
        
        // select user's id
        resultSet =
          UsersIndex.i().executeSyncSelectUser(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            this.airportCode,
            i);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract user's id
        currSelectedUserId =
          resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
        
        // send post photo request
        currControllerCallLog =
          this.backendClientSession.executeControllersCalls(
            RestCallsType.SYNC,
            new ControllerCallPostPhoto(
              new RequestPostPhoto(
                kDeviceToken,
                currSelectedUserId,
                kAccessToken,
                kPhoto,
                kPhoto,
                "",
                "",
                RandomGeneratorInl.generateRandomDouble(
                  this.latitude - kUserLocationRange,
                  this.latitude + kUserLocationRange),
                RandomGeneratorInl.generateRandomDouble(
                  this.longitude - kUserLocationRange,
                  this.longitude + kUserLocationRange) ) ) ).get(0);
        
        // fail?
        if (currControllerCallLog.getResponseHttpStatusCode() !=
              HttpURLConnection.HTTP_OK) {
          
          continue;
        }
        
        // extract post's id
        
        currResponse = (ResponsePostPhoto)currControllerCallLog.getResponse();
        currPostId = UUID.fromString(currResponse.post_id);
        
        // store post in posts index
        PostsIndex.i().executeSyncInsert(
          CalendarFormatterInl.concatCalendarFields(
            this.currentCalendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH),
          this.airportCode,
          seqId,
          currPostId);
        
        seqId += 1;
        
        // increment posts count
        PostsCount.i().executeSyncIncrement(
          CalendarFormatterInl.concatCalendarFields(
            this.currentCalendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
          + "_"
          + this.airportCode);
      } // for (int i = 1; i <= currUsersCount; i ++) {
    } // for (Calendar calendar : this.startTillYesterdayCalendars) {
    
    // reset sequence id
    seqId = 1;
    
    // 2- 60% of the star users created in the past post a photo
    
    // for each day since the start of this service till yesterday
    for (Calendar calendar : this.startTillYesterdayCalendars) {
      
      // select the count of users created on that day in this city
      resultSet =
        UsersCount.i().executeSyncSelect(
          CalendarFormatterInl.concatCalendarFields(
            calendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
            + "_"
            + kStarUserPrefix
            + "_"
            + this.airportCode);
      
      if (resultSet.isExhausted() == true) {
        
        continue;
      }
      
      // extract the count of users created on that day in this city
      currUsersCount =
        (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
      
      // post photos
      for (int i = 1; i <= currUsersCount; i ++) {
        
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 5);
        
        // 60% of the star users only post a new photo
        if (currRandom > 3) {
          
          continue;
        }
        
        // select user's id
        resultSet =
          UsersIndex.i().executeSyncSelectUser(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            kStarUserPrefix
              + "_"
              + this.airportCode,
            i);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract user's id
        currSelectedUserId =
          resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
        
        // send post photo request
        currControllerCallLog =
          this.backendClientSession.executeControllersCalls(
            RestCallsType.SYNC,
            new ControllerCallPostPhoto(
              new RequestPostPhoto(
                kDeviceToken,
                currSelectedUserId,
                kAccessToken,
                kPhoto,
                kPhoto,
                "",
                "",
                RandomGeneratorInl.generateRandomDouble(
                  this.latitude - kStarUserLocationRange,
                  this.latitude + kStarUserLocationRange),
                RandomGeneratorInl.generateRandomDouble(
                  this.longitude - kStarUserLocationRange,
                  this.longitude + kStarUserLocationRange) ) ) ).get(0);
        
        // fail?
        if (currControllerCallLog.getResponseHttpStatusCode() !=
              HttpURLConnection.HTTP_OK) {
          
          continue;
        }
        
        // extract post's id
        
        currResponse = (ResponsePostPhoto)currControllerCallLog.getResponse();
        currPostId = UUID.fromString(currResponse.post_id);
        
        // store post in posts index
        PostsIndex.i().executeSyncInsert(
          CalendarFormatterInl.concatCalendarFields(
            this.currentCalendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH),
          kStarUserPrefix
            + "_"
            + this.airportCode,
          seqId,
          currPostId);
        
        seqId += 1;
        
        // increment posts count
        PostsCount.i().executeSyncIncrement(
          CalendarFormatterInl.concatCalendarFields(
            this.currentCalendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
          + "_"
          + kStarUserPrefix
          + "_"
          + this.airportCode);
      } // for (int i = 1; i <= currUsersCount; i ++) {
    } // for (Calendar calendar : this.startTillYesterdayCalendars) {
  }
  
  /**
   * 5- interactWithPosts
   *      some of the users created today interact with posts from the past week
   *   
   *   1- for users' posts from this city. for each day (week ago till today)
   *        interaction goes as follows
   *        - day 1 (week ago)
   *            10% of the posts get liked by 5-10% of the users
   *            5% of the posts get commented on by 1-2% of the users
   *        - gradually increment ...
   *        - day 7 (today)
   *            70% of the posts get liked by 35-70% of the users
   *            35% of the posts get commented on by 7-14% of the users
   *   2- for users' posts from other cities (30% of the cities). for each
   *        day (week ago till yesterday) interaction goes as follows
   *        - day 1 (week ago)
   *          2% of the posts get liked by 1-5% of the users
   *          1% of the posts get commented on by 1-2.5% of the users
   *        - gradually increment ...
   *        - day 6 (yesterday)
   *          12% of the posts get liked by 6-30% o the users
   *          6% of the posts get commented on by 3-15% of the users
   *   3- for star users' posts from this city. for each day (week ago till
   *        today) interaction goes as follows
   *        - day 1 (week ago)
   *          65% of the posts get liked by 60-65% of the users
   *          20% of the posts get commented on by 15-20% of the users
   *        - gradually increment ...
   *        - day 7 (today)
   *          95% of the posts get liked by 90-95% of the users
   *          50% of the posts get commented on by 45-50% of the users
   *   4- for star users' posts from other cities (70% of the cities). for each
   *        day (week ago till yesterday) interaction goes as follows
   *        - day 1 (week ago)
   *          15% of the posts get liked by 10-15% of the users
   *          5% of the posts get commented on by 2-3% of the users
   *        - gradually increment ...
   *        - day 6 (yesterday)
   *          40% of the posts get liked by 35-40% of the users
   *          30% of the posts get commented on by 12-18% of the users
   * @param createdUsers
   * @param createdStarUsers
   * @throws Exception
   */
  private void interactWithPosts (
    Set<String> createdUsers) throws Exception {
    
    List<String> createdUsersList = new ArrayList<String>(createdUsers);
    
    // current random is used to get probabilities (10%, 30%, ...)
    int currRandom;
    Calendar calendar;
    ResultSet resultSet;
    int currPostsCount;
    boolean commentOnPost;
    String currSelectedPostId;
    ArrayList<ControllerCall> currControllerCalls;
    
    // 1- users interact with users' posts from this city
    for (int indexDay= 1;
         indexDay <= this.lastWeekTillTodayCalendars.size();
         indexDay ++) {
      
      calendar = this.lastWeekTillTodayCalendars.get(indexDay - 1);
      
      // select the count of posts on that day in this city
      resultSet =
        PostsCount.i().executeSyncSelect(
          CalendarFormatterInl.concatCalendarFields(
            calendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
            + "_"
            + this.airportCode);
      
      if (resultSet.isExhausted() == true) {
        
        continue;
      }
      
      // extract the count of posts on that day in this city
      currPostsCount =
        (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
      
      // interact with posts
      for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
        
        // posts percentage for like/comment
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
        
        // only a certain percentage of posts get liked
        if (currRandom > (indexDay * 10) ) {
          
          continue;
        }
        
        // comment on post?
        if (currRandom > (indexDay * 5) ) {
          
          commentOnPost = false;
        } else {
          
          commentOnPost = true;
        }
        
        // select post's id
        resultSet =
          PostsIndex.i().executeSyncSelectPost(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            this.airportCode,
            indexPost);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract post's id
        currSelectedPostId =
          resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
        
        // users percentage to like
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size() / 20) * indexDay),
            Math.max(
              2,
              (createdUsersList.size() / 10) * indexDay) );
        
        // shuffle users
        Collections.shuffle(createdUsersList);
        
        // make like post calls
        
        currControllerCalls = new ArrayList<ControllerCall>();
        
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallLikePost(
              new RequestLikePost(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId) ) );
        }
        
        // don't comment on this post?
        if (commentOnPost == false) {
          
          // send post's likes requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
          
          continue;
        }
        
        // add comment on post calls
        for (int indexUser = 0; indexUser < (currRandom / 2); indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallComment(
              new RequestComment(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId,
                kComment) ) );
        }
        
        // send post's likes and comments requests
        this.backendClientSession.executeControllersCalls(
          RestCallsType.ASYNC,
          currControllerCalls.toArray(new ControllerCall[0] ) );
      } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
    } /* for (int indexDay= 1;
       *      indexDay <= this.lastWeekTillTodayCalendars.size();
       *      indexDay ++) {
       */
    
    // 2- users interact with users' posts from 30% of the other cities
    for (String airportCode : CityBotsLoader.getAirportCodes() ) {
      
      // skip this city, doing other cities only
      if (airportCode.compareTo(this.airportCode) == 0) {
        
        continue;
      }
      
      currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
      
      // posts from 30% of the cities only
      if (currRandom > 3) {
        
        continue;
      }
      
      // for each day from a week ago till yesterday
      for (int indexDay= 1;
          indexDay <= this.lastWeekTillYesterdayCalendars.size();
          indexDay ++) {
        
        calendar = this.lastWeekTillYesterdayCalendars.get(indexDay - 1);
        
        // select the count of posts on that day in this city
        resultSet =
          PostsCount.i().executeSyncSelect(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH)
              + "_"
              + airportCode);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract the count of posts on that day in this city
        currPostsCount =
          (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
        
        // interact with posts
        for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
          
          // posts percentage for like/comment
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
          
          // only a certain percentage of posts get liked
          if (currRandom > (indexDay * 2) ) {
            
            continue;
          }
          
          // comment on post?
          if (currRandom > (indexDay * 1) ) {
            
            commentOnPost = false;
          } else {
            
            commentOnPost = true;
          }
          
          // select post's id
          resultSet =
            PostsIndex.i().executeSyncSelectPost(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH),
              airportCode,
              indexPost);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract post's id
          currSelectedPostId =
            resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
          
          // users percentage to like
          currRandom =
            RandomGeneratorInl.generateRandomInteger(
              Math.max(
                1,
                (createdUsersList.size() / 100) * indexDay),
              Math.max(
                2,
                (createdUsersList.size() / 20) * indexDay) );
          
          // shuffle users
          Collections.shuffle(createdUsersList);
          
          // make like post calls
          
          currControllerCalls = new ArrayList<ControllerCall>();
          
          for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
            
            if (indexUser >= createdUsersList.size() ) {
              
              break;
            }
            
            currControllerCalls.add(
              new ControllerCallLikePost(
                new RequestLikePost(
                  kDeviceToken,
                  createdUsersList.get(indexUser),
                  kAccessToken,
                  currSelectedPostId) ) );
          }
          
          // don't comment on this post?
          if (commentOnPost == false) {
            
            // send post's likes requests
            this.backendClientSession.executeControllersCalls(
              RestCallsType.ASYNC,
              currControllerCalls.toArray(new ControllerCall[0] ) );
            
            continue;
          }
          
          // add comment on post calls
          for (int indexUser = 0; indexUser < (currRandom / 2); indexUser ++) {
            
            if (indexUser >= createdUsersList.size() ) {
              
              break;
            }
            
            currControllerCalls.add(
              new ControllerCallComment(
                new RequestComment(
                  kDeviceToken,
                  createdUsersList.get(indexUser),
                  kAccessToken,
                  currSelectedPostId,
                  kComment) ) );
          }
          
          // send post's likes and comments requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
        } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++)
      } /* for (int indexDay= 1;
         *      indexDay <= this.lastWeekTillYesterdayCalendars.size();
         *      indexDay ++) {
         */
    } // for (String airportCode : CityBotsLoader.getAirportCodes() ) {
    
    // 3- users interact with star users' posts from this city
    for (int indexDay= 1;
        indexDay <= this.lastWeekTillTodayCalendars.size();
        indexDay ++) {
      
      calendar = this.lastWeekTillTodayCalendars.get(indexDay - 1);
      
      // select the count of posts on that day in this city
      resultSet =
        PostsCount.i().executeSyncSelect(
          CalendarFormatterInl.concatCalendarFields(
            calendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
            + "_"
            + kStarUserPrefix
            + "_"
            + this.airportCode);
      
      if (resultSet.isExhausted() == true) {
        
        continue;
      }
      
      // extract the count of posts on that day in this city
      currPostsCount =
        (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
      
      // interact with posts
      for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
        
        // posts percentage for like/comment
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
        
        // only a certain percentage of posts get liked
        if (currRandom > (60 + (indexDay * 5) ) ) {
          
          continue;
        }
        
        // comment on post?
        if (currRandom > (15 + (indexDay * 5) ) ) {
          
          commentOnPost = false;
        } else {
          
          commentOnPost = true;
        }
        
        // select post's id
        resultSet =
          PostsIndex.i().executeSyncSelectPost(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            kStarUserPrefix
              + "_"
              + this.airportCode,
            indexPost);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract post's id
        currSelectedPostId =
          resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
        
        // users percentage to like
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (55 + (indexDay * 5) ) ),
            Math.max(
              2,
              (60 + (indexDay * 5) ) ) );
        
        // shuffle users
        Collections.shuffle(createdUsersList);
        
        // make like post calls
        
        currControllerCalls = new ArrayList<ControllerCall>();
        
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallLikePost(
              new RequestLikePost(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId) ) );
        }
        
        // don't comment on this post?
        if (commentOnPost == false) {
          
          // send post's likes requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
          
          continue;
        }
        
        // users percentage to comment
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (10 + (indexDay * 5) ) ),
            Math.max(
              2,
              (15 + (indexDay * 5) ) ) );
        
        // add comment on post calls
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallComment(
              new RequestComment(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId,
                kComment) ) );
        }
        
        // send post's likes and comments requests
        this.backendClientSession.executeControllersCalls(
          RestCallsType.ASYNC,
          currControllerCalls.toArray(new ControllerCall[0] ) );
      } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
    } /* for (int indexDay= 1;
     *      indexDay <= this.lastWeekTillTodayCalendars.size();
     *      indexDay ++) {
     */
    
    // 4- users interact with star users' posts from 70% of the other cities
    for (String airportCode : CityBotsLoader.getAirportCodes() ) {
      
      // skip this city, doing other cities only
      if (airportCode.compareTo(this.airportCode) == 0) {
        
        continue;
      }
      
      currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
      
      // posts from 70% of the cities only
      if (currRandom > 7) {
        
        continue;
      }
      
      // for each day from a week ago till yesterday
      for (int indexDay= 1;
          indexDay <= this.lastWeekTillYesterdayCalendars.size();
          indexDay ++) {
        
        calendar = this.lastWeekTillYesterdayCalendars.get(indexDay - 1);
        
        // select the count of posts on that day in this city
        resultSet =
          PostsCount.i().executeSyncSelect(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH)
              + "_"
              + kStarUserPrefix
              + "_"
              + airportCode);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract the count of posts on that day in this city
        currPostsCount =
          (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
        
        // interact with posts
        for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
          
          // posts percentage for like/comment
          currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
          
          // only a certain percentage of posts get liked
          if (currRandom > (10 + (indexDay * 5) ) ) {
            
            continue;
          }
          
          // comment on post?
          if (currRandom > (indexDay * 5) ) {
            
            commentOnPost = false;
          } else {
            
            commentOnPost = true;
          }
          
          // select post's id
          resultSet =
            PostsIndex.i().executeSyncSelectPost(
              CalendarFormatterInl.concatCalendarFields(
                calendar,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH),
              kStarUserPrefix
                + "_"
                + airportCode,
              indexPost);
          
          if (resultSet.isExhausted() == true) {
            
            continue;
          }
          
          // extract post's id
          currSelectedPostId =
            resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
          
          // users percentage to like
          currRandom =
            RandomGeneratorInl.generateRandomInteger(
              Math.max(
                1,
                (((createdUsersList.size() / 20) * indexDay)
                + (createdUsersList.size() / 20) ) ),
              Math.max(
                2,
                (((createdUsersList.size() / 20) * indexDay)
                + (createdUsersList.size() / 10) ) ) );
          
          // shuffle users
          Collections.shuffle(createdUsersList);
          
          // make like post calls
          
          currControllerCalls = new ArrayList<ControllerCall>();
          
          for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
            
            if (indexUser >= createdUsersList.size() ) {
              
              break;
            }
            
            currControllerCalls.add(
              new ControllerCallLikePost(
                new RequestLikePost(
                  kDeviceToken,
                  createdUsersList.get(indexUser),
                  kAccessToken,
                  currSelectedPostId) ) );
          }
          
          // don't comment on this post?
          if (commentOnPost == false) {
            
            // send post's likes requests
            this.backendClientSession.executeControllersCalls(
              RestCallsType.ASYNC,
              currControllerCalls.toArray(new ControllerCall[0] ) );
            
            continue;
          }
          
          // users percentage to comment
          currRandom =
            RandomGeneratorInl.generateRandomInteger(
              Math.max(
                1,
                2 * indexDay),
              Math.max(
                2,
                3 * indexDay) );
          
          // add comment on post calls
          for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
            
            if (indexUser >= createdUsersList.size() ) {
              
              break;
            }
            
            currControllerCalls.add(
              new ControllerCallComment(
                new RequestComment(
                  kDeviceToken,
                  createdUsersList.get(indexUser),
                  kAccessToken,
                  currSelectedPostId,
                  kComment) ) );
          }
          
          // send post's likes and comments requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
        } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
      } /* for (int indexDay= 1;
       *      indexDay <= this.lastWeekTillYesterdayCalendars.size();
       *      indexDay ++) {
       */
    } // for (String airportCode : CityBotsLoader.getAirportCodes() ) {
  }
  
  /**
   * 6- interactWithPosts
   *      some of the users created in the past in this city interact with
   *        posts from today in their city and from yesterday in other cities
   * 
   *   1- make a list consisting of some of the users created in this city in
   *        the past, percentage is defined as 100% - week count (i.e.: first
   *        week will be 100 - 1 = 99% and week # 42 will be 100 - 42 = 58%)
   *   2- for users' posts from today in this city. interaction goes as follows
   *      - likes for 100% - week count of the posts and half of that for
   *          comments
   *      - from ((100% - week count) / 10) to ((100% - week count) / 5) of the
   *          users like the post
   *      - from ((100% - week count) / 30) to ((100% - week count) / 15) of the
   *          users comment on the post
   *   3- for users' posts from yesterday in 30% of the other cities.
   *        interaction goes as follows
   *      - likes for 60% - week count of the posts and half of that for
   *          comments
   *      - from ((100% - week count) / 25) to ((100% - week count) / 15) of the
   *          users like the post
   *      - from ((100% - week count) / 50) to ((100% - week count) / 30) of the
   *          users comment on the post
   *   4- for star users' posts from today in this city. interaction goes as
   *        follows
   *      - likes for 100% - (week count / 2) of the posts and half of that for
   *          comments
   *      - from ((100% - week count) / 2) to ((100% - week count) / 1) of the
   *          users like the post
   *      - from ((100% - week count) / 6) to ((100% - week count) / 3) of the
   *          users comment on the post
   *   5- for star users' posts from yesterday in 60% of the other cities.
   *        interaction goes as follows
   *      - likes for 100% - week count of the posts and half of that for
   *          comments
   *      - from ((100% - week count) / 4) to ((100% - week count) / 2) of the
   *          users like the post
   *      - from ((100% - week count) / 12) to ((100% - week count) / 6) of the
   *          users comment on the post
   * @throws Exception
   */
  private void interactWithPosts () throws Exception {
    
    // current random is used to get probabilities (10%, 30%, ...)
    int currRandom;
    ResultSet resultSet;
    int currUsersCount;
    String currSelectedUserId;
    int currPostsCount;
    boolean commentOnPost;
    String currSelectedPostId;
    ArrayList<ControllerCall> currControllerCalls;
    
    // 1- make a list of (100 - week count)% of the users created in this city
    
    List<String> createdUsersList = new ArrayList<String>();
    
    for (Calendar calendar : this.lastWeekTillYesterdayCalendars) {
      
      // select the count of users created on that day in this city
      resultSet =
        UsersCount.i().executeSyncSelect(
          CalendarFormatterInl.concatCalendarFields(
            calendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
            + "_"
            + this.airportCode);
      
      if (resultSet.isExhausted() == true) {
        
        continue;
      }
      
      // extract the count of users created on that day in this city
      currUsersCount =
        (int)resultSet.one().getLong(UsersCount.kUsersCountColumnName);
      
      // get users' ids
      for (int i = 1; i <= currUsersCount; i ++) {
        
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
        
        // only get some of the users
        if (currRandom > (100 - this.weekCount) ) {
          
          continue;
        }
        
        // select user's id
        resultSet =
          UsersIndex.i().executeSyncSelectUser(
            CalendarFormatterInl.concatCalendarFields(
              calendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            this.airportCode,
            i);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract user's id
        currSelectedUserId =
          resultSet.one().getUUID(UsersIndex.kUserIdColumnName).toString();
        
        // add user's id
        createdUsersList.add(currSelectedUserId);
      } // for (int i = 1; i <= currUsersCount; i ++) {
    } // for (Calendar calendar : this.lastWeekTillYesterdayCalendars) {
    
    // 2- users interact with users' posts from today in this city
    
    // select the count of posts on that day in this city
    resultSet =
      PostsCount.i().executeSyncSelect(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH)
          + "_"
          + this.airportCode);
    
    if (resultSet.isExhausted() == false) {
    
      // extract the count of posts on that day in this city
      currPostsCount =
        (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
      
      // interact with posts
      for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
        
        // posts percentage for like/comment
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
        
        // only a certain percentage of posts get liked
        if (currRandom > (100 - this.weekCount) ) {
          
          continue;
        }
        
        // comment on post?
        if (currRandom > ((100 - this.weekCount) / 2) ) {
          
          commentOnPost = false;
        } else {
          
          commentOnPost = true;
        }
        
        // select post's id
        resultSet =
          PostsIndex.i().executeSyncSelectPost(
            CalendarFormatterInl.concatCalendarFields(
              this.currentCalendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            this.airportCode,
            indexPost);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract post's id
        currSelectedPostId =
          resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
        
        // users percentage to like
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 10) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 5) ) ) ) );
        
        // shuffle users
        Collections.shuffle(createdUsersList);
        
        // make like post calls
        
        currControllerCalls = new ArrayList<ControllerCall>();
        
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallLikePost(
              new RequestLikePost(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId) ) );
        }
        
        // don't comment on this post?
        if (commentOnPost == false) {
          
          // send post's likes requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
          
          continue;
        }
        
        // users percentage to comment
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 30) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 15) ) ) ) );
        
        // add comment on post calls
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallComment(
              new RequestComment(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId,
                kComment) ) );
        }
        
        // send post's likes and comments requests
        this.backendClientSession.executeControllersCalls(
          RestCallsType.ASYNC,
          currControllerCalls.toArray(new ControllerCall[0] ) );
      } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
    } // if (resultSet.isExhausted() == false) {
    
    // 3- users interact with users' posts from yesterday in 30% of the other
    //      cities
    for (String airportCode : CityBotsLoader.getAirportCodes() ) {
      
      // skip this city, doing other cities only
      if (airportCode.compareTo(this.airportCode) == 0) {
        
        continue;
      }
      
      currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
      
      // posts from 30% of the cities only
      if (currRandom > 3) {
        
        continue;
      }
      
      // select the count of posts from yesterday in the current city
      resultSet =
        PostsCount.i().executeSyncSelect(
          CalendarFormatterInl.concatCalendarFields(
            this.yesterdayCalendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
            + "_"
            + airportCode);
      
      if (resultSet.isExhausted() == true) {
        
        continue;
      }
    
      // extract the count of posts on that day in this city
      currPostsCount =
        (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
      
      // interact with posts
      for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
        
        // posts percentage for like/comment
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
        
        // only a certain percentage of posts get liked
        if (currRandom > (60 - this.weekCount) ) {
          
          continue;
        }
        
        // comment on post?
        if (currRandom > ((60 - this.weekCount) / 2) ) {
          
          commentOnPost = false;
        } else {
          
          commentOnPost = true;
        }
        
        // select post's id
        resultSet =
          PostsIndex.i().executeSyncSelectPost(
            CalendarFormatterInl.concatCalendarFields(
              this.yesterdayCalendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            airportCode,
            indexPost);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract post's id
        currSelectedPostId =
          resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
        
        // users percentage to like
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 25) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 15) ) ) ) );
        
        // shuffle users
        Collections.shuffle(createdUsersList);
        
        // make like post calls
        
        currControllerCalls = new ArrayList<ControllerCall>();
        
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallLikePost(
              new RequestLikePost(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId) ) );
        }
        
        // don't comment on this post?
        if (commentOnPost == false) {
          
          // send post's likes requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
          
          continue;
        }
        
        // users percentage to comment
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 50) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 30) ) ) ) );
        
        // add comment on post calls
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallComment(
              new RequestComment(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId,
                kComment) ) );
        }
        
        // send post's likes and comments requests
        this.backendClientSession.executeControllersCalls(
          RestCallsType.ASYNC,
          currControllerCalls.toArray(new ControllerCall[0] ) );
      } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
    } // for (String airportCode : CityBotsLoader.getAirportCodes() ) {
    
    // 4- users interact with star users' posts from today in this city
    
    // select the count of posts on that day in this city
    resultSet =
      PostsCount.i().executeSyncSelect(
        CalendarFormatterInl.concatCalendarFields(
          this.currentCalendar,
          Calendar.YEAR,
          Calendar.MONTH,
          Calendar.DAY_OF_MONTH)
          + "_"
          + kStarUserPrefix
          + "_"
          + this.airportCode);
    
    if (resultSet.isExhausted() == false) {
    
      // extract the count of posts on that day in this city
      currPostsCount =
        (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
      
      // interact with posts
      for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
        
        // posts percentage for like/comment
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
        
        // only a certain percentage of posts get liked
        if (currRandom > (100 - (this.weekCount / 2) ) ) {
          
          continue;
        }
        
        // comment on post?
        if (currRandom > ((100 - (this.weekCount / 2) ) / 2) ) {
          
          commentOnPost = false;
        } else {
          
          commentOnPost = true;
        }
        
        // select post's id
        resultSet =
          PostsIndex.i().executeSyncSelectPost(
            CalendarFormatterInl.concatCalendarFields(
              this.currentCalendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            kStarUserPrefix
              + "_"
              + this.airportCode,
            indexPost);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract post's id
        currSelectedPostId =
          resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
        
        // users percentage to like
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 2) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 1) ) ) ) );
        
        // shuffle users
        Collections.shuffle(createdUsersList);
        
        // make like post calls
        
        currControllerCalls = new ArrayList<ControllerCall>();
        
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallLikePost(
              new RequestLikePost(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId) ) );
        }
        
        // don't comment on this post?
        if (commentOnPost == false) {
          
          // send post's likes requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
          
          continue;
        }
        
        // users percentage to comment
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 6) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 3) ) ) ) );
        
        // add comment on post calls
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallComment(
              new RequestComment(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId,
                kComment) ) );
        }
        
        // send post's likes and comments requests
        this.backendClientSession.executeControllersCalls(
          RestCallsType.ASYNC,
          currControllerCalls.toArray(new ControllerCall[0] ) );
      } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
    } // if (resultSet.isExhausted() == false) {
    
    // 5- users interact with star users' posts from yesterday in 60% of the
    //      other cities
    for (String airportCode : CityBotsLoader.getAirportCodes() ) {
      
      // skip this city, doing other cities only
      if (airportCode.compareTo(this.airportCode) == 0) {
        
        continue;
      }
      
      currRandom = RandomGeneratorInl.generateRandomInteger(1, 10);
      
      // posts from 60% of the cities only
      if (currRandom > 6) {
        
        continue;
      }
      
      // select the count of posts from yesterday in the current city
      resultSet =
        PostsCount.i().executeSyncSelect(
          CalendarFormatterInl.concatCalendarFields(
            this.yesterdayCalendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH)
            + "_"
            + kStarUserPrefix
            + "_"
            + airportCode);
      
      if (resultSet.isExhausted() == true) {
        
        continue;
      }
    
      // extract the count of posts on that day in this city
      currPostsCount =
        (int)resultSet.one().getLong(PostsCount.kPostsCountColumnName);
      
      // interact with posts
      for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
        
        // posts percentage for like/comment
        currRandom = RandomGeneratorInl.generateRandomInteger(1, 100);
        
        // only a certain percentage of posts get liked
        if (currRandom > (100 - this.weekCount) ) {
          
          continue;
        }
        
        // comment on post?
        if (currRandom > ((100 - this.weekCount) / 2) ) {
          
          commentOnPost = false;
        } else {
          
          commentOnPost = true;
        }
        
        // select post's id
        resultSet =
          PostsIndex.i().executeSyncSelectPost(
            CalendarFormatterInl.concatCalendarFields(
              this.yesterdayCalendar,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH),
            kStarUserPrefix
              + "_"
              + airportCode,
            indexPost);
        
        if (resultSet.isExhausted() == true) {
          
          continue;
        }
        
        // extract post's id
        currSelectedPostId =
          resultSet.one().getUUID(PostsIndex.kPostIdColumnName).toString();
        
        // users percentage to like
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 4) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 2) ) ) ) );
        
        // shuffle users
        Collections.shuffle(createdUsersList);
        
        // make like post calls
        
        currControllerCalls = new ArrayList<ControllerCall>();
        
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallLikePost(
              new RequestLikePost(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId) ) );
        }
        
        // don't comment on this post?
        if (commentOnPost == false) {
          
          // send post's likes requests
          this.backendClientSession.executeControllersCalls(
            RestCallsType.ASYNC,
            currControllerCalls.toArray(new ControllerCall[0] ) );
          
          continue;
        }
        
        // users percentage to comment
        currRandom =
          RandomGeneratorInl.generateRandomInteger(
            Math.max(
              1,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 12) ) ) ),
            Math.max(
              2,
              (createdUsersList.size()
               / (100 / ((100 - this.weekCount) / 6) ) ) ) );
        
        // add comment on post calls
        for (int indexUser = 0; indexUser < currRandom; indexUser ++) {
          
          if (indexUser >= createdUsersList.size() ) {
            
            break;
          }
          
          currControllerCalls.add(
            new ControllerCallComment(
              new RequestComment(
                kDeviceToken,
                createdUsersList.get(indexUser),
                kAccessToken,
                currSelectedPostId,
                kComment) ) );
        }
        
        // send post's likes and comments requests
        this.backendClientSession.executeControllersCalls(
          RestCallsType.ASYNC,
          currControllerCalls.toArray(new ControllerCall[0] ) );
      } // for (int indexPost= 1; indexPost <= currPostsCount; indexPost ++) {
    } // for (String airportCode : CityBotsLoader.getAirportCodes() ) {
  }

  @Override
  protected void postProcess(CycleLog cycleLog) throws Exception {
    
  }
  
  @Override
  public String toString () {
    
    return
      super.toString()
      + "\n\n Period job's City Bot:\n"
      + "start calendar: "
      + this.startCalendar.getTime().toString()
      + "\nairport code: "
      + this.airportCode
      + "\nlatitude: "
      + this.latitude
      + "\nlongitude: "
      + this.longitude
      + "\npopulation in million: "
      + this.populationInMillions
      + "\ncurrent calendar: "
      + this.currentCalendar.getTime().toString()
      + "\nweek count: "
      + this.weekCount
      + "current backend client session (gets reset before each step): "
      + this.backendClientSession.toString();
  }
}
