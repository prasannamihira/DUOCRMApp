/*
 * Copyright (C) 2017, Duo Software Pvt Ltd.
 *
 * Licensed under the Duo Software;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.duosoftworld.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.duosoft.duocrmtest.network;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by mihira on 6/18/17.
 */

public class NetworkManager {
    private static NetworkManager instance = null;

    private NetworkManager() {

    }

    /**
     * Get NetworkManager single instance
     *
     * @return
     */
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    /**
     * Check internet connection
     *
     * @param contex
     * @return
     */
    public boolean checkInternetConenction(Context contex) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) contex.getSystemService(contex.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}
