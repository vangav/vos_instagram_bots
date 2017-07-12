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

package com.vangav.vos_instagram_bots.cities;

import java.util.Calendar;

import com.datastax.driver.core.ResultSet;
import com.vangav.backend.cassandra.formatting.CalendarFormatterInl;
import com.vangav.backend.files.FileLoaderInl;
import com.vangav.backend.metrics.time.CalendarAndDateOperationsInl;
import com.vangav.vos_instagram_bots.cassandra_keyspaces.ig_bots.Cities;
import com.vangav.vos_instagram_bots.cities.json.CitiesJson;
import com.vangav.vos_instagram_bots.cities.json.CityJson;

/**
 * @author mustapha
 * fb.com/mustapha.abdallah
 */
/**
 * CitiesLoader loads the cities.json from setup_data then uses the loaded
 *   data to update ig_bots.cities table
 */
public class CitiesLoader {

  private static CitiesLoader instance = null;
  
  /**
   * load
   * loads cities.json in the database
   * runs one time only
   * @throws Exception
   */
  public static void load () throws Exception {
   
    if (instance == null) {
      
      instance = new CitiesLoader();
    }
  }
  
  /**
   * Constructor - CitiesLoader
   * @return new CitiesLoader Object
   * @throws Exception
   */
  private CitiesLoader () throws Exception {
    
    this.loadCities();
  }
  
  /**
   * loadCities
   * loads cities.json into the database
   * @throws Exception
   */
  private void loadCities () throws Exception {
    
    // load json file
    CitiesJson citiesJson =
      (CitiesJson)FileLoaderInl.loadJsonFile(new CitiesJson() );
    
    // no cities?
    if (citiesJson.cities == null || citiesJson.cities.length == 0) {
      
      return;
    }
    
    // get current calendar
    Calendar calendar = CalendarAndDateOperationsInl.getCurrentCalendar();
    
    // update ig_bots.cities
    
    ResultSet currResultSet;
    
    for (CityJson cityJson : citiesJson.cities) {
      
      currResultSet = Cities.i().executeSyncSelectCity(cityJson.airport_code);
      
      if (currResultSet.isExhausted() == true) {
        
        Cities.i().executeSyncInsert(
          cityJson.airport_code,
          cityJson.latitude,
          cityJson.longitude,
          cityJson.continent,
          cityJson.continent_code,
          cityJson.country,
          cityJson.country_code,
          cityJson.city,
          cityJson.population_in_millions,
          CalendarFormatterInl.concatCalendarFields(
            calendar,
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH) );
      }
    }
  }
}
