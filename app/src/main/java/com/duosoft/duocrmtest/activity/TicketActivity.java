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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.duosoft.duocrmtest.R;
import com.duosoft.duocrmtest.adapter.TicketAdapter;
import com.duosoft.duocrmtest.adapter.listener.OnItemClickListener;
import com.duosoft.duocrmtest.common.webevents.UserTicketMessage;
import com.duosoft.duocrmtest.model.TicketDto;
import com.duosoft.duocrmtest.model.response.TicketResponse;
import com.duosoft.duocrmtest.network.DuoCrmService;
import com.duosoft.duocrmtest.util.Preference;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihira on 6/19/17.
 */
public class TicketActivity extends BaseActivity implements UserTicketMessage, OnItemClickListener {

    private RecyclerView rvTicket;

    private View mProgressView;
    private View mTicketFormView;

    private TicketAdapter ticketAdapter;
    private LinearLayoutManager mLayoutManager;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<TicketDto> ticketList = new ArrayList<>();
    private TicketResponse ticket = null;
    private TicketDto ticketDto;
    private Subscription subscription;
    private int pageNumber = 1;

    boolean success = false;
    public static String requestToken;

    private Map<Integer, String> mapTicketId;

    private static final String TAG = "TicketActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        initViews();
    }


    // Initialize UI controls
    private void initViews() {

        rvTicket = (RecyclerView) findViewById(R.id.recycler_view_ticket);

        rvTicket.setHasFixedSize(true);

        // set layout manager to recycle view
        mLayoutManager = new LinearLayoutManager(this);

        rvTicket.setLayoutManager(mLayoutManager);

        ticketAdapter = new TicketAdapter(TicketActivity.this, ticketList);

        ticketAdapter.setClickListener(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_tickets);

        mTicketFormView = findViewById(R.id.rl_ticket);
        mProgressView = findViewById(R.id.ticket_progress);

        // Adds the scroll listener to RecyclerView
        rvTicket.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    pageNumber = pageNumber + 1;
                    getTickets(pageNumber);
                }
            }
        });

        getTickets(pageNumber);

    }

    /**
     * Get tickets
     *
     * @param pageNumber
     */
    private void getTickets(int pageNumber) {

        showProgress(true);

        try {
            requestToken = "Bearer " + Preference.showPreference(getResources().getString(R.string.sp_login_token), TicketActivity.this);

            Observable<JsonObject> ticketResponseObservable = DuoCrmService.getInstance().getUserTickets(requestToken, 10, pageNumber);

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
                            showProgress(false);
                            onFailureTickets(e);

                        }

                        @Override
                        public void onNext(JsonObject ticketResponse) {
                            showProgress(false);
                            onSuccessTickets(ticketResponse);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add ticket item data to list
     *
     * @param id
     * @param subject
     * @param type
     * @param priority
     * @param status
     */
    private void fillData(String id, String subject, String type, String priority, String status) {
        ticketDto = new TicketDto(id, subject, type, priority, status);
        ticketList.add(ticketDto);
    }

    @Override
    public void onClick(View view, int position) {

        navigateDynamicActivity(TicketDetailsActivity.class, mapTicketId.get(position));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mTicketFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mTicketFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mTicketFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mTicketFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onSuccessTickets(JsonObject ticketResponse) {
        mapTicketId = new HashMap<Integer, String>();

        try {

            JSONObject obj = new JSONObject(ticketResponse.toString());
            JSONArray jsonArray = obj.getJSONArray("Result");
            for (int j = 0; j < jsonArray.length(); j++) {
                String ticketId = jsonArray.getJSONObject(j).getString("_id");
                String subject = jsonArray.getJSONObject(j).getString("subject");
                String type = jsonArray.getJSONObject(j).getString("type");
                String priority = jsonArray.getJSONObject(j).getString("priority");
                String status = jsonArray.getJSONObject(j).getString("status");

                fillData(ticketId
                        , subject
                        , type
                        , priority
                        , status);

                mapTicketId.put(j, ticketId);

                Log.e("Ticket id : ", ticketId);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        rvTicket.setAdapter(ticketAdapter);
    }

    @Override
    public void onFailureTickets(Throwable e) {
        if (e instanceof HttpException) {
            HttpException response = (HttpException) e;
            int code = response.code();
        }

        Log.e("TAG", "On-Error" + e.getMessage());
    }
}
