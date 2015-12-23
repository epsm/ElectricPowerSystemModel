package test.java.com.epsm.electricPowerSystemModel.model.manualTesting;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ObjectToBeDispatching;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;

public class DispatcherTestImpl implements Dispatcher{

	@Override
	public void acceptReport(PowerObjectState state) {
		System.out.println(state);
	}

	@Override
	public void registerPowerObject(ObjectToBeDispatching powerObject) {
		powerObject.sendReports();
	}
}
