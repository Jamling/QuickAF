/*
 *
 *  * Copyright (C) 2015-2016 QuickAF
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package cn.ieclipse.af.demo;

/**
 * App constants definition
 *
 * @author Jamling
 */
public final class AppConstants {
    private AppConstants() {

    }

    /**
     * {@link android.content.SharedPreferences} key
     */
    public static class Prefs {
        public static final String KEY_UUID = "device.uuid";
        public static String KEY_USER_TOKEN = "key_user_token";
    }
}
