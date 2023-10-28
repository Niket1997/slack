package org.niket.models;

import lombok.Data;

@Data
public class Membership {
  private Integer id;
  private Integer userId;
  private Integer channelId;
  private Boolean isStarred;
  private Boolean isMuted;
  private Long checkpoint;
  private Long createdAt;
  private Long updatedAt;
  private Long deletedAt;
}
