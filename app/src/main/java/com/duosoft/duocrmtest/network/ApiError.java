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

/**
 * Created by mihira on 6/18/17.
 */

public class ApiError {

    private int status_code;
    private String message;

    public ApiError() {

    }

    public int getStatusCode() {

        return status_code;
    }

    public String getMessage() {

        return message;
    }
}
