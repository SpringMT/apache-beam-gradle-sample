package sample.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData implements Serializable {
  @JsonProperty("user_id")
  @NonNull
  private String userId;

  @JsonProperty("shard_id")
  @NonNull
  private Integer shardId;
}
