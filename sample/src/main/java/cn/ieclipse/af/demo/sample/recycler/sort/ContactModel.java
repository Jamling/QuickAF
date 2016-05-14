/*
 * Copyright 2014-2016 QuickAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ieclipse.af.demo.sample.recycler.sort;

import java.util.List;

/**
 * Created by jiang on 16/2/2.
 */
public class ContactModel {


    public List<HouseItemInfo> lipro;

    public static class HouseItemInfo  implements Comparable<HouseItemInfo>{

        public int id;
        public String filing;
        public int status;
        public String title = "";
        public String developerTelephone;

        // 首字母
        private String initial = " ";
        private String spelling = title;

        public void setInitial(String initial) {
            this.initial = initial;
        }

        public String getInitial() {
            return initial;
        }

        public void setSpelling(String spelling) {
            this.spelling = spelling;
        }

        public String getSpelling() {
            return spelling;
        }

        @Override
        public int compareTo(HouseItemInfo another) {
            if (another != null) {
                return getInitial().compareTo(another.getInitial());
            }
            return 0;
        }
    }
}
