package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

public interface ReportSenderSource {
	public Report getReport();
	public void setReportSender(ReportSender sender);
}
