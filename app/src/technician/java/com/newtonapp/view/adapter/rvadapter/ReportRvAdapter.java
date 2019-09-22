package com.newtonapp.view.adapter.rvadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.rvmodel.ReportRvModel;

import java.util.ArrayList;

public class ReportRvAdapter extends RecyclerView.Adapter<ReportRvAdapter.ViewHolder> {

    private ArrayList<ReportRvModel> reportList;
    private OnClickListener onClickListener;

    public ReportRvAdapter(ArrayList<ReportRvModel> reportList) {
        this.reportList = reportList;
    }

    public void setData(ArrayList<ReportRvModel> reportList) {
        this.reportList = reportList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reportItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(reportItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportRvModel report = reportList.get(position);
        holder.setReport(report);
        holder.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        if (reportList == null) return 0;
        else return reportList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvReportDate;
        AppCompatTextView tvReportIDPrinter;
        AppCompatTextView tvReportIDCustomer;
        AppCompatTextView tvReportStatus;
        OnClickListener onClickListener;
        ReportRvModel report;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReportDate = itemView.findViewById(R.id.report_tv_date);
            tvReportIDPrinter = itemView.findViewById(R.id.report_tv_idprinter);
            tvReportIDCustomer = itemView.findViewById(R.id.report_tv_idcustomer);
            tvReportStatus = itemView.findViewById(R.id.report_tv_status);
            itemView.setOnClickListener(view -> onClickListener.onClick(report));
        }

        private void setReport(ReportRvModel report) {
            this.report = report;
            notifyDataSetChanged();
        }

        private void notifyDataSetChanged() {
            tvReportDate.setText(report.getIssueddate());
            tvReportIDPrinter.setText(report.getIdprinter());
            tvReportIDCustomer.setText(report.getIdcustomer());
            tvReportStatus.setText(report.getStatus());
            tvReportStatus.setTextColor(getStatusColor());
        }

        private void setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        private int getStatusColor() {
            int statusColor = R.color.status_hold;
            if (report.getStatus().equalsIgnoreCase(itemView.getContext().getString(R.string.status_solved)))
                statusColor = R.color.status_solved;
            return itemView.getContext().getResources().getColor(statusColor);
        }
    }

    public interface OnClickListener {
        void onClick(ReportRvModel report);
    }
}
