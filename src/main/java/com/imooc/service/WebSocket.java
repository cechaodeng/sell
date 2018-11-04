package com.imooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Kent
 * @date 2017-11-12.
 */
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        log.info("[webSocket消息]有新的连接,总数:{}", webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        log.info("[webSocket消息]连接断开,总数:{}", webSockets.size());
    }

    @OnMessage
    public void OnMessage(String message) {
        log.info("[webSocket消息]收到客户端发来的消息:{}", message);
    }

    /**
     * 广播发送webSocket消息
     * @param message
     */
    public void sendMessage(String message) {
        for (WebSocket webSocket : webSockets) {
            log.info("[webSocket消息]广播消息,message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
