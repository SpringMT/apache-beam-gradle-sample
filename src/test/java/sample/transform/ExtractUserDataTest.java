package sample.transform;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.junit.jupiter.api.Test;
import sample.application.dto.UserData;

class ExtractUserDataTest {
  @Test
  public void testExtractUsers() throws Exception {
    TestPipeline p = TestPipeline.create().enableAbandonedNodeEnforcement(false);

    PCollection<String> input =
        p.apply(
            Create.of(
                    "["
                        + "{\"user_id\": \"aaa\", \"shard_id\": 1},"
                        + "{\"user_id\": \"bbb\", \"shard_id\": 2}"
                        + "]")
                .withCoder(StringUtf8Coder.of()));
    PCollection<UserData> output = input.apply(new ExtractUserData());

    PAssert.that(output).containsInAnyOrder(new UserData("aaa", 1), new UserData("bbb", 2));
    p.run().waitUntilFinish();
  }
}
