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
package com.duosoft.duocrmtest.common.webevents;

import com.google.gson.JsonObject;

/**
 * Created by mihira on 6/19/17.
 */

public interface TicketDetailMessage {

    /**
     * @param ticketResponse
     */
    void onSuccessTickets(JsonObject ticketResponse);

    /**
     * @param error
     */
    void onFailureTickets(Throwable error);
}
