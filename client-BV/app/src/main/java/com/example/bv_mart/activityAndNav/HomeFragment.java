package com.example.bv_mart.activityAndNav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.bv_mart.R;


public class HomeFragment extends Fragment {
    private View view;
    private CardView pay, order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        order = view.findViewById(R.id.get_order);
        pay = view.findViewById(R.id.PayBill);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), StoreGoodsActivity.class));
            }
        });

        return view;
    }

}
