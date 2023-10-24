package org.niket.services;

import org.niket.entities.Channel;
import org.niket.exceptions.EntityNotFoundException;
import org.niket.interfaces.IChannelService;
import org.niket.records.channel.UpsertChannelRequest;
import org.niket.repositories.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChannelService implements IChannelService {
    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(UpsertChannelRequest request) {
        Channel channel = new Channel();
        channel.setName(request.name());
        channel.setDescription(request.description());
        channel.setChannelType(request.channelType());
        return channelRepository.save(channel);
    }

    @Override
    public Channel getChannel(Integer channelId) {
        Optional<Channel> channelOptional = channelRepository.findById(channelId);
        if (channelOptional.isEmpty())
            throw new EntityNotFoundException("no channel found for given channel id: " + channelId);
        return channelOptional.get();
    }

    @Override
    public Channel updateChannel(Integer channelId, UpsertChannelRequest request) {
        Optional<Channel> channelOptional = channelRepository.findById(channelId);
        if (channelOptional.isEmpty())
            throw new EntityNotFoundException("no channel found for given channel id: " + channelId);
        Channel channel = channelOptional.get();
        channel.setName(request.name());
        channel.setDescription(request.description());
        channel.setChannelType(request.channelType());
        channel.markUpdated();
        return channelRepository.save(channel);
    }
}
