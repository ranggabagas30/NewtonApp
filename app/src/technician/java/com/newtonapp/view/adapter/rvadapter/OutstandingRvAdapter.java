package com.newtonapp.view.adapter.rvadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.devs.readmoreoption.ReadMoreOption;
import com.newtonapp.R;
import com.newtonapp.model.rvmodel.OutstandingRvModelNew;

import java.util.ArrayList;

public class OutstandingRvAdapter extends BaseSingleViewTypeRvAdapter<OutstandingRvModelNew, OutstandingRvAdapter.ViewHolder> {

    private Context context;
    private OnCallPicListener onCallPicListener;
    private ReadMoreOption readMoreOption;
    private int textLineTolerance = 3;

    public OutstandingRvAdapter(Context context, ArrayList<OutstandingRvModelNew> data) {
        super(data, R.layout.item_outstanding_task);

        this.context = context;

        readMoreOption = new ReadMoreOption.Builder(context)
                .textLength(textLineTolerance, ReadMoreOption.TYPE_LINE)
                .moreLabel(context.getString(R.string.label_more))
                .lessLabel(context.getString(R.string.label_less))
                .moreLabelColor(context.getResources().getColor(R.color.color_more_label))
                .lessLabelColor(context.getResources().getColor(R.color.color_less_label))
                .labelUnderLine(true)
                .expandAnimation(true)
                .build();
    }

    public void setOnCallPicListener(OnCallPicListener onCallPicListener) {
        this.onCallPicListener = onCallPicListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OutstandingRvModelNew outstanding) {
        holder.setCustomer(outstanding.getCustomer().getNama());
        holder.setAddress(outstanding.getCustomer().getAlamat());
        holder.setProblemDesc(TextUtils.isEmpty(outstanding.getCustomer().getProblems().get(0).getNote())?
                context.getString(R.string.error_problem_desc_unavailable) : outstanding.getCustomer().getProblems().get(0).getNote());
        holder.setPicName(TextUtils.isEmpty(outstanding.getCustomer().getPic())?
                context.getString(R.string.error_pic_name_unavailable) : outstanding.getCustomer().getPic());
        holder.getBtnCallPic().setText(TextUtils.isEmpty(outstanding.getCustomer().getNoTelp())?
                        context.getString(R.string.error_pic_phone_unavailable) : outstanding.getCustomer().getNoTelp());
        holder.getBtnCallPic().setOnClickListener(view -> {
            if (onCallPicListener != null) {
                onCallPicListener.onCall(outstanding.getCustomer().getNoTelp());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView customer;
        private AppCompatTextView address;
        private AppCompatTextView problemDesc;
        private AppCompatTextView picName;
        private AppCompatButton callPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer    = itemView.findViewById(R.id.outstanding_item_tv_customer);
            address     = itemView.findViewById(R.id.outstanding_item_tv_address);
            problemDesc = itemView.findViewById(R.id.outstanding_item_tv_problem_desc);
            picName     = itemView.findViewById(R.id.outstanding_item_tv_pic_name);
            callPic     = itemView.findViewById(R.id.outstanding_item_tv_pic_telp);
        }

        private void setCustomer(String customer) {
            this.customer.setText(customer);
        }

        private void setAddress(String address) {
            this.address.setText(address);
        }

        private void setProblemDesc(String problemDesc) {
            this.problemDesc.setText(problemDesc);
            this.problemDesc.post(() -> {
                int lines = this.problemDesc.getLineCount();
                if (lines >= textLineTolerance) readMoreOption.addReadMoreTo(this.problemDesc, problemDesc);
            });
        }

        private void setPicName(String picName) { this.picName.setText(picName); }

        private AppCompatButton getBtnCallPic() {
            return callPic;
        }
    }

    public interface OnCallPicListener {
        void onCall(String picContactNumber);
    }
}
