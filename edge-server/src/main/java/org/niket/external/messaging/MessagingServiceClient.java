package org.niket.external.messaging;

import java.util.List;
import org.niket.models.Membership;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "messaging-service-client", url = "${external.messaging.base-url}")
public interface MessagingServiceClient {
  @GetMapping("${external.messaging.getChannelsForUserRoute.endpoint}")
  List<Membership> getChannelsForUser(@PathVariable("userId") Integer userId);
}
