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

package com.vangav.vos_instagram_bots.clients;

import java.util.HashMap;
import java.util.Map;

import com.vangav.backend.properties.PropertiesFile;
import com.vangav.backend.properties.PropertiesLoader;

/**
 * Generated using ClientsGeneratorInl
 */
/**
 * ClientsProperties maps clients_properties.prop properties file which
 *   primarily has links to this service's clients
 */
public class ClientsProperties extends PropertiesFile {

  private static ClientsProperties instance = null;

  // disable default instantiation
  private ClientsProperties () {}

  /**
   * i
   * singleton instance method
   * @return ClientsProperties' singleton instance
   */
  public static ClientsProperties i () {

    if (instance == null) {

      instance = new ClientsProperties();
    }

    return instance;
  }

  private static final String kName = "clients_properties";

  @Override
  public String getName () {

    return kName;
  }
  // properties names
  public static final String kVosInstagram = "vos_instagram";

  // property name -> property default value
  private static final Map<String, String> kProperties;
  static {

    kProperties = new HashMap<String, String>();

    kProperties.put(
      kVosInstagram,
      "http://localhost:9000/");
  }

  @Override
  protected String getProperty (String name) throws Exception {

    return PropertiesLoader.i().getProperty(
      this.getName(),
      name,
      kProperties.get(name) );
  }
}
