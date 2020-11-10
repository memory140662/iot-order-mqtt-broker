package com.hy.iot.config;

import com.hy.iot.remote.service.ChtIotService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Objects;

@Configuration
public class RemoteServiceConfig {

    private static Dotenv env = Dotenv.configure().ignoreIfMissing().load();

    private Retrofit chtIotRetrofit = new Retrofit.Builder()
            .baseUrl(Objects.requireNonNull(env.get("CHT_IOT_REMOTE_URL")))
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        val request = chain.request()
                                .newBuilder()
                                .addHeader("X-API-KEY", Objects.requireNonNull(env.get("CHT_IOT_REMOTE_X-API-KEY")))
                                .build();
                        return chain.proceed(request);
                    })
                    .build())
            .build();

    @Bean
    public ChtIotService chtIotService() {
        return chtIotRetrofit.create(ChtIotService.class);
    }

}
