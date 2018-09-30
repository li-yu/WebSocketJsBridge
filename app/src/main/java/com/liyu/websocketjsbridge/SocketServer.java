package com.liyu.websocketjsbridge;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyu on 2018/9/26.
 */
public class SocketServer {

    Activity activity;

    static volatile SocketServer defaultInstance;

    AsyncHttpServer server;

    List<WebSocket> _sockets = new ArrayList<>();

    public static SocketServer getDefault() {
        if (defaultInstance == null) {
            synchronized (SocketServer.class) {
                if (defaultInstance == null) {
                    defaultInstance = new SocketServer();
                }
            }
        }
        return defaultInstance;
    }

    private SocketServer() {

    }

    public void listen(Activity activity, int port) {
        this.activity = activity;
        if (server != null) {
            server.stop();
        }
        server = new AsyncHttpServer();
        server.websocket("/jssdk", new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                _sockets.add(webSocket);

                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        try {
                            if (ex != null)
                                Log.e("WebSocket", "Error");
                        } finally {
                            _sockets.remove(webSocket);
                        }
                    }
                });

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(final String s) {
                        SocketServer.getDefault().activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SocketServer.getDefault().activity, "收到来自 JS 端的消息：" + s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
        server.listen(port);
    }

    public void send(String res) {
        if (server != null) {
            for (WebSocket socket : _sockets)
                socket.send(res);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop();
            defaultInstance = null;
        }
    }
}
