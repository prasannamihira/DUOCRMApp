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

import com.duosoft.duocrmtest.model.request.UserLoginRequest;
import com.duosoft.duocrmtest.model.response.UserLoginResponse;
import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mihira on 6/18/17.
 */

public interface DuoCrmApi {

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })

    /**
     * User login with email and password
     */
    @POST("auth/login")
    Observable<UserLoginResponse> userLoginRequest(@Body UserLoginRequest loginRequest);

    /**
     * Get user tickets
     *
     * @param token
     * @param limit
     * @param pageNumber
     * @return
     */
    @GET("DVP/API/1.0.0.0/MyTickets/{limit}/{pageNumber}?status=new")
    Observable<JsonObject> getUserTickets(@Header("Authorization") String token, @Path("limit") int limit, @Path("pageNumber") int pageNumber);

    /**
     * Get user ticket in details
     *
     * @param token
     * @param id
     * @return
     */
    @GET("DVP/API/1.0.0.0/Ticket/{id}/Details")
    Observable<JsonObject> getUserTicketDetails(@Header("Authorization") String token, @Path("id") String id);

}
