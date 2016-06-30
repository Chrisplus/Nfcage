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

import android.util.Log;

/**
 * Created by chrisplus on 30/6/16.
 */
public abstract class NfcageListener {
    public static final String TAG = NfcageListener.class.getSimpleName();

    public abstract void onComplete(Smartcard card);

    public abstract void onError(Throwable error);

    public void onNext(NfcageReader.Status status) {
        Log.d(TAG, status.getStatusCode() + "");
    }
}
