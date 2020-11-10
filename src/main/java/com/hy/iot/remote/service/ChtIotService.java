package com.hy.iot.remote.service;

import lombok.Data;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChtIotService {

    @POST("/bigdata/v1/prediction/{modelId}")
    Call<PredictionResponse> prediction(
            @Path("modelId") String modelId,
            @Body PredictionRequest body
    );

    @Data
    class PredictionResponse {
        private String value;
    }

    @Data
    class PredictionRequest {
        private int[] features;
    }
}
