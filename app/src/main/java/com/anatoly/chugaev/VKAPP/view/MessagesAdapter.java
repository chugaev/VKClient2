package com.anatoly.chugaev.VKAPP.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anatoly.chugaev.VKAPP.R;
import com.anatoly.chugaev.VKAPP.network.Model.Conversation.Message;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.OnLoadMoreListener;

import java.util.List;

import static com.anatoly.chugaev.VKAPP.utils.Utils.getFormatTime;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 0;
    private static final int VIEW_TYPE_LOADING = 2;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    private OnLoadMoreListener onLoadMoreListener;

    private List<Message> mMessages;

    public MessagesAdapter(RecyclerView recyclerView, List<Message> messages) {
        this.mMessages = messages;
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

    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        if (message == null) {
            return VIEW_TYPE_LOADING;
        } else {
            return mMessages.get(position).getOut();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.sent_message_row, viewGroup, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recieved_message_row, viewGroup, false);
            return new ReceivedMessageHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.progress_bar, viewGroup, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);

        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_LOADING:
                ((LoadingViewHolder) viewHolder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {

        private TextView mText;
        private TextView mDate;
        private ImageView mDone;

        public SentMessageHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
            mDate = itemView.findViewById(R.id.date);
            mDone = itemView.findViewById(R.id.done);
        }

        void bind(Message message) {
            mText.setText(message.getText());
            mDate.setText(getFormatTime(message.getDate()));
            mDone.setImageResource(R.mipmap.all_done);
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        private TextView mText;
        private TextView mDate;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
            mDate = itemView.findViewById(R.id.date);
        }

        void bind (Message message) {
            mText.setText(message.getText());
            mDate.setText(getFormatTime(message.getDate()));
        }
    }
}
