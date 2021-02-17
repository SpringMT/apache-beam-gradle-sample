package sample.transform;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.api.services.bigquery.model.TimePartitioning;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;
import sample.application.dto.UserData;

@AllArgsConstructor
public class ImportUserData extends PTransform<PCollection<UserData>, PDone> {
  private final String projectId;

  static class UserDataSchemaFactory {
    public static TableSchema create() {
      List<TableFieldSchema> fields;
      fields = new ArrayList<>();
      fields.add(new TableFieldSchema().setName("user_id").setType("STRING").setMode("REQUIRED"));
      return new TableSchema().setFields(fields);
    }
  }

  @Override
  public PDone expand(PCollection<UserData> input) {
    TableSchema schema = UserDataSchemaFactory.create();
    String analyticsLogBigQueryTableName = String.format("%s:foo.user_data", this.projectId);
    PCollection<TableRow> tableRow = input.apply(ParDo.of(new ConvertBigQueryRow()));
    tableRow.apply(
        "WriteToBigQuery",
        BigQueryIO.writeTableRows()
            .to(analyticsLogBigQueryTableName)
            .withSchema(schema)
            .withTimePartitioning(new TimePartitioning().setType("DAY").setField("date"))
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));
    return PDone.in(tableRow.getPipeline());
  }

  public static class ConvertBigQueryRow extends DoFn<UserData, TableRow> {
    @ProcessElement
    public void processElement(@Element UserData userData, OutputReceiver<TableRow> receiver) {
      TableRow output = new TableRow();
      output.set("user_id", userData.getUserId());
      receiver.output(output);
    }
  }
}
