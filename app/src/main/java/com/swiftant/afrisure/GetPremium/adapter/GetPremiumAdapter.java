package com.swiftant.afrisure.GetPremium.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swiftant.afrisure.GetPremium.model.GetPremiumModel;
import com.swiftant.afrisure.R;

import java.util.List;

public class GetPremiumAdapter extends RecyclerView.Adapter<GetPremiumAdapter.ViewHolder> {
    private final List<GetPremiumModel> premiumList;
    private Context context;
    onItemSelectListener listener;

    public GetPremiumAdapter(List<GetPremiumModel> premiumList, Context context, onItemSelectListener listener) {
        this.premiumList = premiumList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_premium_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetPremiumModel premiumModel = premiumList.get(position);

        holder.productName.setText(premiumModel.getProductName());
        holder.basicPremium.setText(premiumModel.getBasicPremium());
        holder.addonsPremium.setText(premiumModel.getAddonsPremium());
        holder.discountPremium.setText(premiumModel.getDiscountPremium());
        holder.beforeTax.setText(premiumModel.getBeforeTax());
        holder.taxPremium.setText(premiumModel.getTaxPremium());
        holder.totalPremium.setText(premiumModel.getTotalPremium());
//        if(premiumModel.getDiscountPremium().equals("KSh 0.00")){
//            holder.discountLayout.setVisibility(View.GONE);
//        }else{
//            holder.discountLayout.setVisibility(View.VISIBLE);
//        }

        if (premiumModel.isSelected) {
            changeColor(holder.selectedBtn, R.color.greyDark);
            holder.selectedBtnText.setText("Selected");
        } else {
            changeColor(holder.selectedBtn, R.color.themeColor);
            holder.selectedBtnText.setText("Select");
        }

        holder.selectedBtn.setOnClickListener(onClickSelect -> {
            if (listener != null) {
                if (!premiumModel.isSelected) {
                    premiumModel.isSelected = true;
                    listener.itemSelected(premiumModel, position);
                } else {
                    premiumModel.isSelected = false;
                    listener.itemUnSelected(position);
                }
            }
        });
    }

    void changeColor(LinearLayout linearLayout, int color) {
        linearLayout.setBackgroundTintList(ColorStateList.valueOf(context.getColor(color)));
    }

    @Override
    public int getItemCount() {
        return premiumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, basicPremium, addonsPremium, discountPremium, beforeTax, taxPremium, totalPremium;
        LinearLayout discountLayout;
        TextView selectedBtnText;
        LinearLayout selectedBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            basicPremium = itemView.findViewById(R.id.basicPremium);
            addonsPremium = itemView.findViewById(R.id.addOnPremium);
            discountPremium = itemView.findViewById(R.id.discountPremium);
            beforeTax = itemView.findViewById(R.id.beforeTax);
            taxPremium = itemView.findViewById(R.id.taxPremium);
            totalPremium = itemView.findViewById(R.id.totalPremium);
            discountLayout = itemView.findViewById(R.id.discountLayout);
            selectedBtn = itemView.findViewById(R.id.selectBtn);
            selectedBtnText = itemView.findViewById(R.id.selectBtnText);

        }
    }

    public interface onItemSelectListener {
        void itemSelected(GetPremiumModel premiumModel, int position);

        void itemUnSelected(int pos);
    }
}

