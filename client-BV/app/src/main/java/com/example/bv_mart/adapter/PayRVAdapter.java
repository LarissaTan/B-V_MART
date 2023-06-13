package com.example.bv_mart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bv_mart.R;
import com.example.bv_mart.bean.GoodsArrayBean;

import java.util.List;



public class PayRVAdapter  extends RecyclerView.Adapter<PayRVAdapter.ViewHolder> {

    private LayoutInflater inflater ;
    private List<GoodsArrayBean.ItemR> data;

    public PayRVAdapter(List<GoodsArrayBean.ItemR> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pay_order_goods_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (data.get(position).getPicNumb()){
            case "0":
                holder.iv_pic.setImageResource(R.drawable.c1);
                break;
            case "1":
                holder.iv_pic.setImageResource(R.drawable.c2);
                break;
            case "2":
                holder.iv_pic.setImageResource(R.drawable.c3);
                break;
            case "3":
                holder.iv_pic.setImageResource(R.drawable.c4);
                break;
            case "4":
                holder.iv_pic.setImageResource(R.drawable.soda);
                break;
            case "5":
                holder.iv_pic.setImageResource(R.drawable.cake);
                break;
            case "6":
                holder.iv_pic.setImageResource(R.drawable.cookie);
                break;
        }

        holder.tv_name.setText(data.get(position).getName());
        holder.tv_price.setText(data.get(position).getPrice());
        holder.tv_number.setText("X "+data.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        private ImageView iv_pic;
        private TextView tv_name ;
        private TextView tv_price ;
        private TextView tv_number ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_pic = itemView.findViewById(R.id.iv_pay_goods);
            tv_name = itemView.findViewById(R.id.tv_pay_Name);
            tv_price = itemView.findViewById(R.id.tv_pay_Price);
            tv_number = itemView.findViewById(R.id.tv_pay_number);
        }
    }
}
