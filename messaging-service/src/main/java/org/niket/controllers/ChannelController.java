package org.niket.controllers;

import org.niket.entities.Channel;
import org.niket.interfaces.IChannelService;
import org.niket.records.channel.UpsertChannelRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/channels")
public class ChannelController {
    private final IChannelService channelService;

    public ChannelController(IChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("")
    public Channel createChannel(@RequestBody UpsertChannelRequest request) {
        return channelService.createChannel(request);
    }

    @PutMapping("/{channelId}")
    public Channel updateChannel(@PathVariable Integer channelId, @RequestBody UpsertChannelRequest request) {
        return channelService.updateChannel(channelId, request);
    }

    @GetMapping("/{channelId}")
    public Channel getChannel(@PathVariable Integer channelId) {
        return channelService.getChannel(channelId);
    }

    @DeleteMapping("/{channelId}")
    public void deleteMessage(@PathVariable Integer channelId) {
        channelService.deleteChannel(channelId);
    }
}
