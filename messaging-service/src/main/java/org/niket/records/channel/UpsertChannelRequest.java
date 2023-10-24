package org.niket.records.channel;

import org.niket.enums.ChannelType;

public record UpsertChannelRequest(
        String name,
        String description,
        ChannelType channelType
) {
}
