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

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by mihira on 6/19/17.
 */

public class DuoCrmService {

    private DuoCrmApi duoCrmApi;
    private static DuoCrmService duoCrmService;

    public static DuoCrmService getInstance() {
        if (duoCrmService == null)
            duoCrmService = new DuoCrmService();
        return duoCrmService;
    }

    /**
     * Network connection configurations
     */
    private DuoCrmService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(rxAdapter)
                .baseUrl(DuoCrmApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        try {
            this.duoCrmApi = retrofit.create(DuoCrmApi.class);
        } catch (Exception ex) {
            Log.d("DuoCrmApi Exception", ex.toString());
        }
    }

    /**
     * Get tickets by user login token
     *
     * @param token
     * @param limit
     * @param pageNumber
     * @return
     */
    public Observable<JsonObject> getUserTickets(String token, int limit, int pageNumber) {
        return this.duoCrmApi.getUserTickets(token, limit, pageNumber);
    }

    /**
     * Get user ticket details
     *
     * @param token
     * @param id
     * @return
     */
    public Observable<JsonObject> getUserTicketDetails(String token, String id) {
        return this.duoCrmApi.getUserTicketDetails(token, id);
    }


}
