package com.anatoly.chugaev.VKAPP.view;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anatoly.chugaev.VKAPP.R;
import com.anatoly.chugaev.VKAPP.app.Devices;
import com.anatoly.chugaev.VKAPP.network.Model.Conversation.Conversation;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.ConvAndUser;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.OnLoadMoreListener;
import com.anatoly.chugaev.VKAPP.utils.ProductDiffUtilCallback;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.anatoly.chugaev.VKAPP.utils.Utils.getFormatTime;

public class ConversationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ConvAndUser> mConvAndUsers;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public ConversationsAdapter(RecyclerView recyclerView,
                                List<ConvAndUser> convAndUsers) {
        this.mConvAndUsers = convAndUsers;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mConvAndUsers.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test2, parent, false);
            return new ConversationsHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConversationsHolder) {
            Conversation conversation = mConvAndUsers.get(position).getConversation();
            ConversationsHolder conversationsHolder = (ConversationsHolder) holder;
            conversationsHolder.image.setImageResource(R.mipmap.default_photo);
            conversationsHolder.platform.setImageResource(0);
            conversationsHolder.name.setText("");
            conversationsHolder.message.setText("");
            conversationsHolder.time.setText("");
            conversationsHolder.checkMark.setImageResource(0);
            conversationsHolder.unreadCount.setVisibility(View.INVISIBLE);
            if (conversation.getConv().getPeer().getType().equals("user")) {
                conversationsHolder.name.setText(
                        new StringBuilder()
                                .append(mConvAndUsers.get(position).getUser().getFirstName())
                                .append(" ")
                                .append(mConvAndUsers.get(position).getUser().getLastName())
                                .toString());
                if (mConvAndUsers.get(position).getUser().getLastSeen() != null) {
                    if (mConvAndUsers.get(position).getUser().getOnline() == 1) {
                        switch (Devices.values()[mConvAndUsers
                                .get(position)
                                .getUser()
                                .getLastSeen()
                                .getPlatform() - 1]) {
                            case MOBILE:
                                conversationsHolder.platform.setImageResource(R.mipmap.phone);
                                if (mConvAndUsers.get(position).getUser().getOnlineApp() != null) {
                                    if (mConvAndUsers.get(position).getUser().getOnlineApp()
                                            == 2685278) {
                                        conversationsHolder.platform.setImageResource(R.mipmap.kate);
                                    }
                                }
                                break;
                            case IPHONE:
                                conversationsHolder.platform.setImageResource(R.mipmap.apple);
                                break;
                            case IPAD:
                                conversationsHolder.platform.setImageResource(R.mipmap.apple);
                                break;
                            case ANDOROID:
                                conversationsHolder.platform.setImageResource(R.mipmap.android);
                                break;
                            case WINDOWSPHONE:
                                conversationsHolder.platform.setImageResource(R.mipmap.windows);
                                break;
                            case WINDOWS10:
                                conversationsHolder.platform.setImageResource(R.mipmap.windows);
                                break;
                            case PC:
                                conversationsHolder.platform.setImageResource(R.mipmap.pc);
                            default:
                                break;
                        }
                    }
                }
                if (!"".equals(mConvAndUsers.get(position).getUser().getDeleted())
                        && mConvAndUsers.get(position).getUser().getDeleted() != null) {
                    conversationsHolder.image.setImageResource(R.mipmap.banned_user);
                } else if (mConvAndUsers.get(position).getUser().getPhoto200() != null) {
                    Picasso.with(conversationsHolder.image.getContext())
                            .load(mConvAndUsers.get(position).getUser().getPhoto200())
                            .into(conversationsHolder.image);
                }
            } else if (conversation.getConv().getPeer().getType().equals("chat")) {
                conversationsHolder.name.setText(mConvAndUsers.get(position)
                .getConversation().getConv().getChatSettings().getTitle());
                if (mConvAndUsers.get(position)
                        .getConversation().getConv().getChatSettings().getPhoto() != null) {
                    Picasso.with(conversationsHolder.image.getContext())
                            .load(mConvAndUsers.get(position)
                                    .getConversation().getConv().getChatSettings().getPhoto().getPhoto200())
                            .into(conversationsHolder.image);
                }
            } else if (conversation.getConv().getPeer().getType().equals("group")) {
                conversationsHolder.name.setText(mConvAndUsers.get(position)
                        .getGroup().getName());
                Picasso.with(conversationsHolder.image.getContext())
                        .load(mConvAndUsers.get(position)
                                .getGroup().getPhoto200())
                        .into(conversationsHolder.image);
            }
            if (mConvAndUsers.get(position).getConversation().getConv().getUnreadCount() != 0) {
                conversationsHolder.unreadCount.setText(String.valueOf(mConvAndUsers
                        .get(position).getConversation().getConv().getUnreadCount()));
                conversationsHolder.unreadCount.setVisibility(View.VISIBLE);
            } else {
                if (mConvAndUsers.get(position).getConversation().getConv().getOutId() !=
                        mConvAndUsers.get(position).getConversation().getMessage().getId()) {
                    conversationsHolder.checkMark.setImageResource(R.mipmap.done);
                } else {
                    if (mConvAndUsers.get(position).getConversation().getMessage().getOut() == 1) {
                        conversationsHolder.checkMark.setImageResource(R.mipmap.all_done);
                    }
                }
            }
            conversationsHolder.message.setText(conversation.getMessage().getText());
            Calendar mydate = Calendar.getInstance();
            mydate.setTimeInMillis((long) conversation.getMessage().getDate() * 1000);
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            if (mydate.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)
                    && mydate.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    && mydate.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                conversationsHolder.time.setText("вчера");
            } else if (mydate.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    && mydate.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
                    && mydate.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                conversationsHolder.time.setText(getFormatTime(conversation.getMessage().getDate()));
            } else {
                conversationsHolder.time.setText(
                        mydate.get(Calendar.DAY_OF_MONTH)
                                + " "
                                + getMonthForInt(mydate.get(Calendar.MONTH)));
            }
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return mConvAndUsers == null ? 0 : mConvAndUsers.size();
    }

    private String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public void setLoaded() {
        isLoading = false;
    }

    class ConversationsHolder extends RecyclerView.ViewHolder {
        private TextView name, message, time;
        private CircleImageView image;
        private ImageView platform;
        private TextView unreadCount;
        private ImageView checkMark;

        public ConversationsHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            message = view.findViewById(R.id.message);
            time = view.findViewById(R.id.time);
            image = view.findViewById(R.id.photo_profile);
            platform = view.findViewById(R.id.platform);
            unreadCount = view.findViewById(R.id.unread_count);
            checkMark = view.findViewById(R.id.check_mark);
        }
    }

    public void setData(List<ConvAndUser> newData) {
        ProductDiffUtilCallback productDiffUtilCallback
                = new ProductDiffUtilCallback(mConvAndUsers, newData);
        DiffUtil.DiffResult convDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        convDiffResult.dispatchUpdatesTo(this);
        mConvAndUsers = newData;
    }
}


