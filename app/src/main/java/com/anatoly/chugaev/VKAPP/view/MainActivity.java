package com.anatoly.chugaev.VKAPP.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.anatoly.chugaev.VKAPP.SettingsActivity;
import com.anatoly.chugaev.VKAPP.utils.MyDividerItemDecorator;
import com.anatoly.chugaev.VKAPP.R;
import com.anatoly.chugaev.VKAPP.network.ApiClient;
import com.anatoly.chugaev.VKAPP.network.Model.Conversation.Conversation;
import com.anatoly.chugaev.VKAPP.network.Model.Group.Group;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.ConvAndUser;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.OnLoadMoreListener;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.User;
import com.anatoly.chugaev.VKAPP.app.Account;
import com.anatoly.chugaev.VKAPP.app.Constants;
import com.anatoly.chugaev.VKAPP.network.VkApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main";

    private RecyclerView mRecyclerView;
    private ConversationsAdapter mAdapter;
    private Account mAccount;
    private VkApi mVkApi;
    private int countDialogs;
    private int offset= 0;
    private Toolbar mToolbar;
    private boolean firstLaunch = true;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<ConvAndUser> mConvAndUsers = new ArrayList<>();
        List<ConvAndUser> mConvAndUsersNew = new ArrayList<>();
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mAccount = Account.get(this);
        mAccount.restore(this);
        mVkApi = ApiClient.getClient().create(VkApi.class);
        mRecyclerView = findViewById(R.id.recycler_view);
        setTitle(R.string.connecting);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyDividerItemDecorator(this, LinearLayoutManager.VERTICAL, 16));
        mAdapter = new ConversationsAdapter(mRecyclerView, mConvAndUsers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, mConvAndUsers.size() + " " + countDialogs);
                if (mConvAndUsers.size() < countDialogs) {
                    mConvAndUsers.add(null);
                    mRecyclerView.post(new Runnable() {
                        public void run() {
                            mAdapter.notifyItemInserted(mConvAndUsers.size() - 1);
                        }
                    });
                    offset += 20;
                    getConvserationsAndUsers(mConvAndUsers, 1);
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new RVConversationTouchListener(
                getApplicationContext(),
                mRecyclerView,
                new RVConversationTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ConvAndUser cau = mConvAndUsers.get(position);
                Intent intent = MessageActivity.newIntent(
                        getApplicationContext(),
                        cau.getUser().getId(),
                        cau.getUser().getFirstName(),
                        cau.getUser().getLastName(),
                        cau.getUser().getPhoto200(),
                        cau.getUser().getLastSeen().getTime());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mAdapter.setLoaded();

        if (mAccount.access_token == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            getConvserationsAndUsers(mConvAndUsers, 1);
        }

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String string = intent.getStringExtra(Constants.PARAM_SERVICE);
                Log.d(TAG, string);
                getConvserationsAndUsers(mConvAndUsersNew, 0);
                mAdapter.setData(mConvAndUsersNew);
            }
        };
        IntentFilter intFilt = new IntentFilter(Constants.BROADCAST_ACTION);
        registerReceiver(br, intFilt);
    }

    private void getConvserationsAndUsers(List<ConvAndUser> convAndUsers, int load) {
        if (load == 0) {
            offset = 0;
        }
        mVkApi.getConversations(20, "all", 1, offset,
                "photo_200,online,last_seen",
                Constants.API_VERSION,
                mAccount.access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    countDialogs = response.getResponse().getCount();
                    List<Conversation> convs = response.getResponse().getConversations();
                    List<User> users = response.getResponse().getUsers();
                    List<Group> groups = response.getResponse().getGroups();
                    if (convAndUsers.size() != 0) {
                        convAndUsers.remove(convAndUsers.size() - 1);
                        mAdapter.notifyItemInserted(convAndUsers.size());
                    }
                    for (Conversation conv : convs) {
                        User user = null;
                        Group group = null;
                        if (conv.getConv().getPeer().getType().equals("user")) {
                            for (User u : users) {
                                if (u.getId() == conv.getConv().getPeer().getId()) {
                                    user = u;
                                    break;
                                }
                            }
                        } else if (conv.getConv().getPeer().getType().equals("group")) {
                            for (Group g : groups) {
                                if (g.getId() == conv.getConv().getPeer().getId() * (-1)) {
                                    group = g;
                                    break;
                                }
                            }
                        }
                        convAndUsers.add(new ConvAndUser(conv, user, group));
                    }
                    if (firstLaunch) {
                        setTitle(R.string.vk);
                    }
                    if (load == 1) {
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setLoaded();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}

