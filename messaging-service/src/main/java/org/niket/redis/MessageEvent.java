package org.niket.redis;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEvent implements Serializable {
  private Integer Id;
  private String text;
  private Integer senderUserId;
  private Integer channelId;
  private Long createdAt;
  private Long updatedAt;
  private Long deletedAt;
}
