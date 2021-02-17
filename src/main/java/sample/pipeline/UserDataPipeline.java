package sample.pipeline;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.transform.*;

public class UserDataPipeline {
  private static final Logger LOG = LoggerFactory.getLogger(UserDataPipeline.class);

  public static void main(String[] args) {
    Options options = OptionsFactory.setupOptions(args);
    options.setJobName("user-data-sync");

    Pipeline pipeline = Pipeline.create(options);

    String userDataSyncTopic =
        String.format("projects/%s/topics/user-data-sync", options.getProject());

    pipeline
        .apply("ReadFromUserDataPubSub", PubsubIO.readStrings().fromTopic(userDataSyncTopic))
        .apply(new ExtractUserData())
        .apply(new ImportUserData(options.getOtherProject()));

    try {
      pipeline.run();
    } catch (StackOverflowError e) {
      Throwable cause = e.getCause();
      if (cause != null) {
        LOG.error(cause.toString());
      }
      LOG.error(e.getMessage());
      StackTraceElement[] stackTraces = e.getStackTrace();
      StringBuilder sb = new StringBuilder();

      for (int i = 1; i < 100; i++) {
        int index = stackTraces.length - i;
        if (index < 0) {
          break;
        }
        StackTraceElement s = stackTraces[index];
        sb.append(
            String.format(
                "\tat %s(%s:%s)%n", s.getMethodName(), s.getFileName(), s.getLineNumber()));
      }
      LOG.error(sb.toString());
      throw e;
    }
  }
}
