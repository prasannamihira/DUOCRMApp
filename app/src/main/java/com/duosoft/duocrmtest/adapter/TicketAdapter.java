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
package com.duosoft.duocrmtest.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duosoft.duocrmtest.R;
import com.duosoft.duocrmtest.adapter.listener.OnItemClickListener;
import com.duosoft.duocrmtest.model.TicketDto;

import java.util.ArrayList;

/**
 * Created by mihira on 6/19/17.
 */

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.Holder> {

    private Activity mactivity;
    private ArrayList<TicketDto> mTicketData;
    private TicketDto ticketDto;
    private OnItemClickListener clickListener;

    /**
     * Constructor TicketAdapter class
     *
     * @param activity
     * @param TicketData
     */
    public TicketAdapter(Activity activity, ArrayList<TicketDto> TicketData) {
        this.mactivity = activity;
        this.mTicketData = TicketData;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mactivity)
                .inflate(R.layout.list_ticket_item, parent, false);
        Holder dataObjectHolder = new Holder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Holder myHolder = (Holder) holder;

        ticketDto = (mTicketData).get(position);

        myHolder.txtSubject.setText(ticketDto.subject);
        myHolder.txtType.setText(ticketDto.type);
        myHolder.txtPriority.setText(ticketDto.priority);
        myHolder.txtstatus.setText(ticketDto.status);

    }

    @Override
    public int getItemCount() {
        return mTicketData.size();
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout rlTicket;
        private TextView txtSubjectLeft, txtSubject, txtTypeLeft, txtType, txtPriorityLeft, txtPriority, txtstatusLeft, txtstatus;
        private ImageView imgStatus;

        public Holder(View itemView) {
            super(itemView);

            rlTicket = (RelativeLayout) itemView.findViewById(R.id.rlTicket);

            txtSubjectLeft = (TextView) itemView.findViewById(R.id.txtSubjectLeft);
            txtSubject = (TextView) itemView.findViewById(R.id.txtSubject);

            txtTypeLeft = (TextView) itemView.findViewById(R.id.txtTypeLeft);
            txtType = (TextView) itemView.findViewById(R.id.txtType);

            txtPriorityLeft = (TextView) itemView.findViewById(R.id.txtPriorityLeft);
            txtPriority = (TextView) itemView.findViewById(R.id.txtPriority);

            txtstatusLeft = (TextView) itemView.findViewById(R.id.txtStatusLeft);
            txtstatus = (TextView) itemView.findViewById(R.id.txtStatus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }
    }
}
