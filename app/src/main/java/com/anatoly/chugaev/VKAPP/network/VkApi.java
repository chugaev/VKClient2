package com.anatoly.chugaev.VKAPP.network;

import com.anatoly.chugaev.VKAPP.network.Model.Conversation.ConversationBaseResponse;
import com.anatoly.chugaev.VKAPP.network.Model.Conversation.MessageBaseResponse;
import com.anatoly.chugaev.VKAPP.network.Model.LongPollServer.DataLongPollServer;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.UsersBaseResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApi {
    @GET("messages.getConversations")
    Observable<ConversationBaseResponse> getConversations(@Query("count") int count,
                                                          @Query("filter") String filter,
                                                          @Query("extended") int extended,
                                                          @Query("offset") Integer startMesId,
                                                          @Query("fields") String fields,
                                                          @Query("v") String version,
                                                          @Query("access_token") String accessToken);
    @GET("users.get")
    Observable<UsersBaseResponse> getUsers(@Query("user_ids") String ids,
                                     @Query("fields") String fields,
                                     @Query("version") String version,
                                     @Query("access_token") String accessToken);

    @GET("messages.getHistory")
    Observable<MessageBaseResponse> getMessages(@Query("user_id") int userId,
                                          @Query("count") Integer count,
                                          @Query("offset") Integer offset,
                                          @Query("v") String version,
                                          @Query("access_token") String accessToken);

    @GET("messages.getLongPollServer")
    Call<DataLongPollServer> getLongPollServer(@Query("need_pts") int needPts,
                                               @Query("lp_version") int lpVersion,
                                               @Query("v") String version,
                                               @Query("access_token") String accessToken);

}
