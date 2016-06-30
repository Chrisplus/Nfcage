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

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;

/**
 * Created by chrisplus on 30/6/16.
 */
public class Nfcage {
    public static final String TAG = Nfcage.class.getSimpleName();

    private static Nfcage instance;
    private NfcAdapter nfcAdapter;

    public synchronized static Nfcage getInstance() {
        if (instance == null) {
            instance = new Nfcage();
        }
        return instance;
    }

    public void onResume(final Activity activity, NfcAdapter adapter) {
        nfcAdapter = adapter;
        onResume(activity);
    }

    public void onPause(final Activity activity, NfcAdapter adapter) {
        nfcAdapter = adapter;
        onPause(activity);
    }

    public void onResume(final Activity activity) {
        final Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];

        String[][] techLists = new String[][]{
                new String[]{IsoDep.class.getName()}
        };

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        getNfcAdapter(activity).enableForegroundDispatch(activity, pendingIntent, filters,
                techLists);
    }

    public void onPause(final Activity activity) {
        getNfcAdapter(activity).disableForegroundDispatch(activity);
    }


    public void onReadCard(Tag tag, NfcageListener listener) {

    }

    public Smartcard onRead(Tag tag) {
        return null;
    }

    private NfcAdapter getNfcAdapter(Context context) {
        if (nfcAdapter == null) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        }

        return nfcAdapter;
    }
}
