package com.anatoly.chugaev.VKAPP.network.Model.Conversation;

import com.google.gson.annotations.SerializedName;

public class Conversation {

    @SerializedName("conversation")
    private Conv mConv;
    @SerializedName("last_message")
    private Message mMessage;

    public Conv getConv() {
        return mConv;
    }

    public Message getMessage() {
        return mMessage;
    }

    public class Conv {
        @SerializedName("peer")
        private Peer mPeer;
        @SerializedName("chat_settings")
        private ChatSettings mChatSettings;
        @SerializedName("last_message_id")
        private Integer lastMessageId;
        @SerializedName("unread_count")
        private int unreadCount;
        @SerializedName("out_read")
        private int outId;

        public Peer getPeer() {
            return mPeer;
        }

        public ChatSettings getChatSettings() {
            return mChatSettings;
        }

        public Integer getLastMessageId() {
            return lastMessageId;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public int getOutId() {
            return outId;
        }

        public class Peer {
            @SerializedName("id")
            private int id;
            @SerializedName("type")
            private String type;

            public String getType() {
                return type;
            }

            public int getId() {
                return id;
            }
        }

        public class ChatSettings {
            @SerializedName("title")
            private String title;
            @SerializedName("photo")
            private MyPhoto mPhoto;

            public String getTitle() {
                return title;
            }

            public MyPhoto getPhoto() {
                return mPhoto;
            }

            public class MyPhoto {
                @SerializedName("photo_50")
                private String photo50;
                @SerializedName("photo_100")
                private String photo100;
                @SerializedName("photo_200")
                private String photo200;

                public String getPhoto50() {
                    return photo50;
                }

                public String getPhoto100() {
                    return photo100;
                }

                public String getPhoto200() {
                    return photo200;
                }
            }
        }
    }
}
