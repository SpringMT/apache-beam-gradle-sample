package sample.pipeline;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

public interface Options extends GcpOptions {
  @Description("GCP Environment")
  @Validation.Required
  String getEnv();

  void setEnv(String env);

  @Description("GCP Project Id for other")
  @Validation.Required
  String getOtherProject();

  void setOtherProject(String projectId);
}
