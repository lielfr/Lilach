package org.cshaifasweng.winter.web;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import org.cshaifasweng.winter.LoggerUtils;
import org.cshaifasweng.winter.events.TokenSetEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TokenInterceptor implements Interceptor {

    protected static final Logger log = Logger.getLogger(TokenInterceptor.class.getName());

    private String token;

    public TokenInterceptor() {
        EventBus.getDefault().register(this);
        token = "";
        LoggerUtils.setupLogger(log);
    }

    @Subscribe
    public void handleTokenSet(TokenSetEvent event) {
        token = event.getToken();
        log.finest("Token set");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        if (!token.isEmpty())
            requestBuilder = requestBuilder.addHeader("Authorization", "Bearer " + token);
        return chain.proceed(requestBuilder.build());
    }
}
