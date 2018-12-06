package com.anatoly.chugaev.VKAPP.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.anatoly.chugaev.VKAPP.R;
import com.anatoly.chugaev.VKAPP.app.Account;
import com.anatoly.chugaev.VKAPP.app.Constants;
import com.anatoly.chugaev.VKAPP.network.ApiClient;
import com.anatoly.chugaev.VKAPP.network.Model.Conversation.Message;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.OnLoadMoreListener;
import com.anatoly.chugaev.VKAPP.network.VkApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.anatoly.chugaev.VKAPP.utils.Utils.getFormatTime;


public class MessageActivity extends AppCompatActivity {

    private static final String EXTRA_FIRST_NAME = "com.anatoly.chugaev.vkmessnager.first_name";
    private static final String EXTRA_LAST_NAME = "com.anatoly.chugaev.vkmessnager.last_name";
    private static final String EXTRA_ID = "com.anatoly.chugaev.vkmessnager.id";
    private static final String EXTRA_PHOTO_200 = "com.anatoly.chugaev.vkmessnager.photo_200";
    private static final String EXTRA_DATE = "com.anatoly.chugaev.vkmessnager.date";

    public static Intent newIntent(Context context,
                                   int id,
                                   String name,
                                   String lastName,
                                   String photo,
                                   long date) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(EXTRA_FIRST_NAME, name);
        intent.putExtra(EXTRA_LAST_NAME, lastName);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_PHOTO_200, photo);
        intent.putExtra(EXTRA_DATE, date);
        return intent;
    }

    private final int COUNT_MESSAGES = 20;

    private List<Message> mMessages = new ArrayList<>();
    private int countMessages = 0;
    private int offset = 0;
    private VkApi mVkApi;
    private Account mAccount;
    private RecyclerView mRecyclerView;
    private MessagesAdapter mAdapter;
    private int id;
    private String firstName;
    private String lastName;
    private String photo;
    private long date;
    private Toolbar mToolbar;
    private CircleImageView headImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        id = getIntent().getExtras().getInt(EXTRA_ID);
        firstName = getIntent().getExtras().getString(EXTRA_FIRST_NAME);
        lastName = getIntent().getExtras().getString(EXTRA_LAST_NAME);
        photo = getIntent().getExtras().getString(EXTRA_PHOTO_200);
        date = getIntent().getExtras().getLong(EXTRA_DATE);
        TextView name = mToolbar.findViewById(R.id.name);
        name.setText(String.format("%s %s", firstName, lastName));
        headImage = mToolbar.findViewById(R.id.image);
        if (photo != null) {
            Picasso.with(getApplicationContext())
                    .load(photo)
                    .into(headImage);
        } else {
            headImage.setImageResource(R.mipmap.default_photo);
        }
        TextView lastSeen = findViewById(R.id.last_seen);
        lastSeen.setText(String.format("last seen at %s", getFormatTime(date)));
        mVkApi = ApiClient.getClient().create(VkApi.class);
        mAccount = Account.get(this);
        mRecyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MessagesAdapter(mRecyclerView, mMessages);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mMessages.size() < countMessages) {
                    mMessages.add(null);
                    mRecyclerView.post(new Runnable() {
                        public void run() {
                            mAdapter.notifyItemInserted(mMessages.size() - 1);
                        }
                    });
                    offset += COUNT_MESSAGES;
                    getMessages();
                }
            }
        });
        getMessages();
    }

    private void getMessages() {
        mVkApi.getMessages(id, COUNT_MESSAGES, offset,
                Constants.API_VERSION,
                mAccount.access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (mMessages.size() != 0) {
                        mMessages.remove(mMessages.size() - 1);
                        mAdapter.notifyItemInserted(mMessages.size());
                    }
                    countMessages = response.getResponse().getCount();
                    mMessages.addAll(response.getResponse().getMessages());
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setLoaded();
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}
