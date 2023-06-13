package com.example.bv_mart.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bv_mart.adapter.ChatMessageAdapter;
import com.example.bv_mart.MainActivity;
import com.example.bv_mart.R;
import com.example.bv_mart.bean.ChatMessageBean;
import com.example.bv_mart.util.AppContext;
import com.example.bv_mart.util.DateUtill;
import com.example.bv_mart.util.MySQLiteHelper;
import com.example.bv_mart.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 评论界面的fragment
 */
public class StoreCommentFragment extends Fragment {

    private List<ChatMessageBean> chatMessageBeans = new ArrayList<>();
    private RecyclerView rv_Chat;
    private ChatMessageAdapter adapter;
    private EditText et_chat_message;
    private Button btn_message_send;
    private String messages;
    private ChatMessageBean chatMessageBean;

    private int MyUserID = MySQLiteHelper.getInstance(AppContext.getInstance()).GetUserId(MainActivity.username);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_comment,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initView();
    }

    private void initData() {

        chatMessageBeans = MySQLiteHelper.getInstance(getContext()).queryAllMessages();

    }

    private void initView() {
        rv_Chat = getActivity().findViewById(R.id.rv_chat);
        et_chat_message = getActivity().findViewById(R.id.et_chat_message);
        btn_message_send = getActivity().findViewById(R.id.btn_message_send);
        messages = et_chat_message.getText().toString();


        adapter = new ChatMessageAdapter(chatMessageBeans);
        rv_Chat.setItemAnimator(new DefaultItemAnimator());
        rv_Chat.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_Chat.setAdapter(adapter);
        rv_Chat.scrollToPosition(adapter.getItemCount()-1);

        //发送信息的判断和操作
        btn_message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages = et_chat_message.getText().toString();
                if (TextUtils.isEmpty(messages)){
                    ToastUtil.showShort("内容为空！");
                }else {
                    chatMessageBean = new ChatMessageBean(MyUserID,MyUserID+"",messages,MainActivity.username, DateUtill.getCurrentTime());
                    MySQLiteHelper.getInstance(getActivity()).insertMessages(chatMessageBean);
                    adapter.refreshMessages();
                    rv_Chat.scrollToPosition(adapter.getItemCount()-1);
                    ToastUtil.showShort("发送成功");
                    et_chat_message.setText("");
                }
            }
        });
    }
}
