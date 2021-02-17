package sample.pipeline;

import lombok.extern.java.Log;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

@Log
public class OptionsFactory {

  public static Options setupOptions(String[] args) {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);

    // NOTE NOT templateLocation !!!!
    options.setTempLocation(
        String.format("gs://%s.appspot.com/dataflow/tmp", options.getProject()));

    options.setWorkerRegion("asia-northeast1");

    log.info("Options: " + options);

    return options;
  }

  private OptionsFactory() {}
}
