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

import java.util.HashMap;
import java.util.List;

/**
 * Created by chrisplus on 30/6/16.
 */
public class Smartcard {
    public static final String TAG = Smartcard.class.getSimpleName();

    private String cardIssuer;
    private String cardName;
    private String cardUid;
    private String cardNo;
    private int cardBalance;
    private List<byte[]> cardTrans;
    private HashMap<String, Byte[]> rawData;


    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardUid() {
        return cardUid;
    }

    public void setCardUid(String cardUid) {
        this.cardUid = cardUid;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }

    public List<byte[]> getCardTrans() {
        return cardTrans;
    }

    public void setCardTrans(List<byte[]> cardTrans) {
        this.cardTrans = cardTrans;
    }
}
