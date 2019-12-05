/*
 * Copyright (C) 2019 César Pomar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ignis.backend.chronos;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 *
 * @author César Pomar
 */
public class ChronosClient {

    private final OkHttpClient client;

    private class ChronosInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            if (!response.isSuccessful()) {
                throw new ChronosException(response.code(), response.toString());
            }
            return response;
        }

    }

    public ChronosClient() {
        OkHttpClient.Builder httpbuilder = new OkHttpClient.Builder();
        httpbuilder.followRedirects(true);
        httpbuilder.addInterceptor(new ChronosInterceptor());
        client = httpbuilder.build();
    }

    public Chronos connect(String url) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(url);
        builder.addConverterFactory(JacksonConverterFactory.create());
        builder.callFactory(client);
        return builder.build().create(Chronos.class);
    }

}
