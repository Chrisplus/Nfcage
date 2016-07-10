/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chrisplus.nfcage;

/**
 * Created by chrisplus on 10/7/16.
 */
public class Constants {
    public static final String TAG = Constants.class.getSimpleName();

    public static class JsonInnerAttr{
        public static final String NAME = "name";
        public static final String ISSUER = "issuer";
        public static final String PROTOCOL = "protocol";
        public static final String APPLICATIONS = "applications";
        public static final String BALANCE = "balance";
        public static final String TRANSACTION = "transaction";
        public static final String RECORD = "record";
        public static final String ID = "id";
        public static final String SFI = "sfi";
        public static final String LOC = "loc";
        public static final String LEN = "len";
        public static final String TYPE = "type";
        public static final String ENDIAN = "endian";
        public static final String COUNT = "count";
    }
}
