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

import android.text.TextUtils;

/**
 * Created by chrisplus on 30/6/16.
 */
public class NfcageReader {
    public static final String TAG = NfcageReader.class.getSimpleName();

    public enum Status {
        HINT(100),
        NEXT(201),
        RESET(301);

        private final static String NULL_MESSAGE = "no custom message";
        private int code;
        private String message;

        private Status(int code) {
            this.code = code;
        }

        public void setCustomMessage(String customMessage) {
            message = customMessage;
        }

        public int getStatusCode() {
            return code;
        }

        public String getMessage() {
            return TextUtils.isEmpty(message) ? NULL_MESSAGE : message;
        }
    }
}
