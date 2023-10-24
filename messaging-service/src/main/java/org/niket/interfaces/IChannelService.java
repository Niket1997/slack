package org.niket.interfaces;

import org.niket.entities.Channel;
import org.niket.records.channel.UpsertChannelRequest;

public interface IChannelService {
    Channel createChannel(UpsertChannelRequest request);

    Channel getChannel(Integer channelId);

    Channel updateChannel(Integer channelId, UpsertChannelRequest request);

    void deleteChannel(Integer channelId);
}
