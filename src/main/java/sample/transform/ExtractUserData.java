package sample.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.PCollection;
import sample.application.dto.UserData;

@AllArgsConstructor
public class ExtractUserData extends PTransform<PCollection<String>, PCollection<UserData>> {
  @Override
  public PCollection<UserData> expand(PCollection<String> users) {
    return users.apply(ParDo.of(new ParseUserData()));
  }

  public static class ParseUserData extends DoFn<String, UserData> {
    @ProcessElement
    public void processElement(@Element String element, OutputReceiver<UserData> receiver)
        throws IOException {
      ObjectMapper objectMapper = new ObjectMapper();
      UserData[] userData = objectMapper.readValue(element, UserData[].class);
      for (UserData userDatum : userData) {
        receiver.output(userDatum);
      }
    }
  }
}
