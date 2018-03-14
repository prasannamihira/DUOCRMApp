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
package com.duosoft.duocrmtest.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.duosoft.duocrmtest.R;
import com.duosoft.duocrmtest.common.webevents.TicketDetailMessage;
import com.duosoft.duocrmtest.network.DuoCrmService;
import com.duosoft.duocrmtest.util.Preference;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihira on 6/19/17.
 */
public class TicketDetailsActivity extends BaseActivity implements TicketDetailMessage {

    private static final String TAG = "TicketDetailsActivity";

    private Subscription subscription;

    boolean success = false;
    private static String requestToken;
    private String ticketId;

    private CoordinatorLayout coordinatorLayout;
    TextView tvSubject, tvType, tvPriority, tvStatus, tvReference, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ticketId = getIntent().getStringExtra("id");


        initViews();

    }

    // Initialize UI controls
    private void initViews() {

        tvSubject = (TextView) findViewById(R.id.txtDetailsSubject);
        tvType = (TextView) findViewById(R.id.txtDetailsType);
        tvPriority = (TextView) findViewById(R.id.txtDetailsPriority);
        tvStatus = (TextView) findViewById(R.id.txtDetailsStatus);
        tvReference = (TextView) findViewById(R.id.txtDetailsReference);
        tvDescription = (TextView) findViewById(R.id.txtDetailsDescription);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_ticket_details);

        getTicketDetails();

    }

    /**
     * Get ticket details
     */
    private void getTicketDetails() {

        try {
            requestToken = "Bearer " + Preference.showPreference(getResources().getString(R.string.sp_login_token), TicketDetailsActivity.this);

            Observable<JsonObject> ticketResponseObservable = DuoCrmService.getInstance().getUserTicketDetails(requestToken, ticketId);

            this.subscription = ticketResponseObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<JsonObject>() {
                        @Override
                        public void onCompleted() {
                            Log.e("TAG", "On-Complete");
                        }

                        @Override
                        public void onError(Throwable e) {
                            onFailureTickets(e);
                        }

                        @Override
                        public void onNext(JsonObject ticketResponse) {
                            onSuccessTickets(ticketResponse);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccessTickets(JsonObject ticketResponse) {

        try {

            JSONObject obj = new JSONObject(ticketResponse.toString());
            JSONObject jsonItem = new JSONObject(obj.getString("Result").toString());

            String ticketId = jsonItem.getString("_id");
            String subject = jsonItem.getString("subject");
            String type = jsonItem.getString("type");
            String priority = jsonItem.getString("priority");
            String status = jsonItem.getString("status");
            String reference = jsonItem.getString("reference");
            String description = jsonItem.getString("description");

            tvSubject.setText(subject);
            tvType.setText(type);
            tvPriority.setText(priority);
            tvStatus.setText(status);
            tvReference.setText(reference);
            tvDescription.setText(description);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureTickets(Throwable error) {

    }
}
