package com.example.bv_mart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bv_mart.fragment.StoreGoodsFragment;
import com.example.bv_mart.R;
import com.example.bv_mart.bean.GoodsArrayBean;
import com.example.bv_mart.util.MyDialog;
import com.example.bv_mart.util.ToastUtil;

import java.math.BigDecimal;
import java.util.List;



public class ShoppongCarGoodsAdapter  extends  RecyclerView.Adapter<ShoppongCarGoodsAdapter.ViewHolder>{


    private LayoutInflater inflater ;
    private List<GoodsArrayBean.ItemR> data;
    public static double total;
    private static BigDecimal b1 ;
    private static BigDecimal b2 ;
    private static BigDecimal b3 ;
    private static BigDecimal result ;
    private static BigDecimal one ;
    private static double a ;


    public ShoppongCarGoodsAdapter(List<GoodsArrayBean.ItemR> data){
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.shopping_car_goods_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (data.get(position).getPicNumb()){
            case "0":
                holder.iv_pic.setImageResource(R.drawable.calendar);
                break;
            case "1":
                holder.iv_pic.setImageResource(R.drawable.chair);
                break;
            case "2":
                holder.iv_pic.setImageResource(R.drawable.jasmine);
                break;
            case "3":
                holder.iv_pic.setImageResource(R.drawable.milk);
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
        holder.tv_number.setText(data.get(position).getNumber()+"");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_pic ;
        private ImageView iv_add ;
        private ImageView iv_minus ;
        private TextView tv_price ;
        private TextView tv_number ;
        private TextView tv_name ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_pic = itemView.findViewById(R.id.iv_car_goods);
            iv_add = itemView.findViewById(R.id.iv_car_add);
            iv_minus = itemView.findViewById(R.id.iv_car_minus);
            tv_price = itemView.findViewById(R.id.tv_car_Price);
            tv_number = itemView.findViewById(R.id.tv_car_number);
            tv_name = itemView.findViewById(R.id.tv_car_Name);

            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.get(getAdapterPosition()).setNumber(data.get(getAdapterPosition()).getNumber()+1);
                    //int p = StoreGoodsFragment.goodsArrayBean.itemsRight.indexOf(data.get(getAdapterPosition()));
                    //StoreGoodsFragment.goodsArrayBean.itemsRight.get(p).setNumber(StoreGoodsFragment.goodsArrayBean.itemsRight.get(p).getNumber()+1);
                    StoreGoodsFragment.handler.sendEmptyMessage(1);
                    notifyItemChanged(getAdapterPosition());
                    refreshTotal();
                }
            });

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.get(getAdapterPosition()).setNumber(data.get(getAdapterPosition()).getNumber()-1);
                    //发送信息，通知商品列表刷新
                    StoreGoodsFragment.handler.sendEmptyMessage(1);
                    refreshTotal();
                    if (data.get(getAdapterPosition()).getNumber() == 0){
                        data.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyDataSetChanged();
                        refreshTotal();
                    }else {
                        notifyItemChanged(getAdapterPosition());
                        refreshTotal();
                    }
                    //如果数据已为空，通知handler刷新
                    if (data.size() == 0){
                        ToastUtil.showShort("Cart is empty");
                        MyDialog.handler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    private void refreshTotal(){
        total = 0;
        for (int i= 0;i<data.size();i++){
            b1 = new BigDecimal(data.get(i).getPrice().trim());
            b2 = new BigDecimal(data.get(i).getNumber());
            b3 = new BigDecimal(total);
            result = b1.multiply(b2);
            result = result.add(b3);
            one = new BigDecimal("1");
            a = result.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位数
            total = a ;

        }
        MyDialog.tv_total.setText("￥ "+ total);
    }

}
