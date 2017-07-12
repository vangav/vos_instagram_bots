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

import java.util.ArrayList;
import java.util.Calendar;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.vangav.backend.metrics.time.CalendarAndDateOperationsInl;
import com.vangav.backend.metrics.time.RoundedOffCalendarInl;
import com.vangav.backend.metrics.time.RoundedOffCalendarInl.RoundingFactor;
import com.vangav.backend.metrics.time.RoundedOffCalendarInl.RoundingType;
import com.vangav.backend.thread_pool.periodic_jobs.PeriodicJobsManager;
import com.vangav.vos_instagram_bots.cassandra_keyspaces.ig_bots.Cities;

/**
 * @author mustapha
 * fb.com/mustapha.abdallah
 */
/**
 * CityBotsLoader loads city bots
 */
public class CityBotsLoader {
  
  private ArrayList<String> airportCodes;

  /**
   * Constructor - CityBotsLoader
   * @return new CityBotsLoader Object
   * @throws Exception
   */
  private CityBotsLoader () throws Exception {
    
    this.airportCodes = new ArrayList<String>();
    
    this.loadCityBots();
  }
  
  private static CityBotsLoader instance = null;
  
  /**
   * load
   * runs once to load and start city bots
   * @throws Exception
   */
  public static void load () throws Exception {
    
    if (instance == null) {
      
      instance = new CityBotsLoader();
    }
  }
  
  /**
   * getAirportCodes
   * @return all airport codes
   * @throws Exception
   */
  protected static ArrayList<String> getAirportCodes () throws Exception {
    
    load();
    
    return instance.airportCodes;
  }
  
  /**
   * loadCityBots
   * loads city bots
   * @throws Exception
   */
  private void loadCityBots () throws Exception {
    
    ResultSet resultSet = Cities.i().executeSyncSelectAll();
    
    if (resultSet.isExhausted() == true) {
      
      return;
    }
    
    Calendar startCalendar = CalendarAndDateOperationsInl.getCurrentCalendar();
    
    startCalendar =
      RoundedOffCalendarInl.getRoundedCalendar(
        startCalendar,
        RoundingType.FUTURE,
        RoundingFactor.DAY_OF_MONTH);
    
    for (Row row : resultSet) {
      
      this.airportCodes.add(row.getString(Cities.kAirportCodeColumnName) );
      
      PeriodicJobsManager.i().registerNewPeriodicJob(
        new CityBot(
          startCalendar,
          row.getString(Cities.kAirportCodeColumnName),
          row.getDouble(Cities.kLatitudeColumnName),
          row.getDouble(Cities.kLongitudeColumnName),
          row.getInt(Cities.kPopulationInMillionsColumnName),
          row.getString(Cities.kAddedYearMonthDayColumnName) ) );
    }
  }
}
