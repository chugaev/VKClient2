package com.anatoly.chugaev.VKAPP.network;

import com.anatoly.chugaev.VKAPP.network.Model.LongPollServer.LongPollResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LongPollApi {
    @GET()
    Call<LongPollResponse> getChanges();
}
