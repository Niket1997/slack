package org.niket.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.niket.exceptions.InvalidRequestException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class SocketModule {
    public SocketModule(SocketIOServer socketIOServer) {
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());
    }

//    private DataListener<Message> onMessageReceived() {
//        return (senderClient, message, ackSender) -> {
//            log.info(message.toString());
//            String room = message.getChannelId().toString();
//            Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(room).getClients();
//            for (SocketIOClient client : clients) {
//                if (!client.getSessionId().equals(senderClient.getSessionId())) {
//                    client.sendEvent("read_message",
//                            message);
//                }
//            }
//        };
//    }

    private ConnectListener onConnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            if (!params.containsKey("userId") || params.get("userId").size() != 1) {
                throw new InvalidRequestException("userId should be present");
            }

            int userId = Integer.parseInt(params.get("userId").get(0));
            // get channels a user is part of
            Set<String> channels = Set.of("1", "2");
            client.joinRooms(channels);
            log.info("## client connected.");
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("** client disconnected.");
        };
    }
}
