package com.example.bv_mart.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.bv_mart.bean.chatObject;
import com.example.bv_mart.util.AppContext;
import com.example.bv_mart.util.DateUtill;
import com.example.bv_mart.util.MySQLiteHelper;
import com.example.bv_mart.util.ToastUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    public Socket socket; // 替换为服务器的IP地址和端口号
    public ObjectOutputStream writer;
    public ObjectInputStream reader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_comment,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // 连接Socket并启动后台线程接收消息
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    socket = new Socket("172.20.10.2", 12345); //10.0.2.2
                    writer = new ObjectOutputStream(socket.getOutputStream());
                    writer.flush();
                    reader = new ObjectInputStream(socket.getInputStream());

                    Log.i("ReceiveMessagesTask", "after setup");
                    // 启动后台线程接收消息
                    ReceiveMessagesTask receiveMessagesTask = new ReceiveMessagesTask();
                    Log.i("ReceiveMessagesTask", "next is execute");
                    receiveMessagesTask.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 在连接完成后的回调方法中执行其他操作
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        initView();
                    }
                }, 500); // 延迟0.5秒执行
            }
        }.execute();

        initData();
        initView();
    }

    private void initData() {
        chatMessageBeans = MySQLiteHelper.getInstance(getContext()).queryAllMessages();
        Log.i("StoreCommentFragment", "init, size is : " + String.valueOf(chatMessageBeans.size()));
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
                Log.i("ReceiveMessagesTask","onclick!");
                messages = et_chat_message.getText().toString();
                if (TextUtils.isEmpty(messages)) {
                    ToastUtil.showShort("内容为空！");
                } else {
                    chatMessageBean = new ChatMessageBean(messages, MainActivity.username, DateUtill.getCurrentTime());

                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            String response = null;
                            try {
                                Log.i("ReceiveMessagesTask", "doInBackground: writer is here"); // 添加日志输出
                                //Socket socket = new Socket("10.0.2.2", 12345); // 替换为服务器的IP地址和端口号
                                //ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
                                //                                // 启动后台线程接收消息
//                                ReceiveMessagesTask receiveMessagesTask = new ReceiveMessagesTask();
//                                receiveMessagesTask.execute();

                                chatObject msg = new chatObject(MainActivity.username,messages,DateUtill.getCurrentTime());
                                Log.i("ReceiveMessagesTask", "doInBackground: msg sending is " + msg.msg); // 添加日志输出
                                writer.writeObject(msg); // 发送消息到服务器
                                writer.flush();

                                //writer.close();
                                //socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return response;
                        }

                        @Override
                        protected void onPostExecute(String response) {
                            // 在网络操作完成后的回调方法中更新UI或执行其他操作
                            MySQLiteHelper.getInstance(getActivity()).insertMessages(chatMessageBean);
                            Log.i("StoreCommentFragment", "writer, size is : " + String.valueOf(chatMessageBeans.size()));
                            adapter.refreshMessages();
                            rv_Chat.scrollToPosition(adapter.getItemCount() - 1);
                            ToastUtil.showShort("发送成功");
                            et_chat_message.setText("");
                        }
                    }.execute();
                }
            }
        });
    }


    // 后台接收消息的异步任务
    private class ReceiveMessagesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("ReceiveMessagesTask", "doInBackground: Task started.");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("ReceiveMessagesTask", "doInBackground: try started.");
                        while (socket.isConnected()) {
                            try {
                                Log.i("ReceiveMessagesTask", "doInBackground: socket.is connected");
                                chatObject receivedMsg = (chatObject) reader.readObject();
                                if (receivedMsg != null) {
                                    ChatMessageBean receivedChatBean = new ChatMessageBean(
                                            receivedMsg.msg, receivedMsg.username, receivedMsg.time);
                                    // 通过回调方法更新UI
                                    onMessageReceived(receivedChatBean);
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } finally {
                        try {
                            if (socket != null) {
                                socket.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            return null;
        }
    }

    // 回调方法，在接收到消息时更新UI
    private void onMessageReceived(ChatMessageBean message) {
        Log.i("StoreCommentFragment", "svt right here");
        // 在这里处理接收到的消息，例如更新聊天界面的列表
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatMessageBeans.add(message);
                Log.i("StoreCommentFragment", "size is : " + String.valueOf(chatMessageBeans.size()));
                adapter.refreshMessages();
                rv_Chat.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }
}
