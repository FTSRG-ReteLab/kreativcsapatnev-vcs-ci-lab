package hu.bme.mit.train.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.interfaces.TrainController;

public class TrainSensorTest {

    private TrainSensorImpl sensor;
    private TrainUser user;
    private TrainController controller;

    @Before
    public void before() {
	controller = mock(TrainController.class);
	user = mock(TrainUser.class);
	sensor = new TrainSensorImpl(controller, user);
    }

    @Test //a regular example, the speed limit increases from 50 to 100 while train moves at 30
    public void NormalOperation() {
	controller.setSpeedLimit(50);
	when(controller.getReferenceSpeed()).thenReturn(30);
	sensor.overrideSpeedLimit(100);
	verify(user).setAlarmState(false);
    }

    @Test //the speed limit becomes 600 which is unreasonably high
    public void TooHighLimit() {
	sensor.overrideSpeedLimit(600);
	verify(user).setAlarmState(true);
    }

    @Test//the speed limit becomes -20, which doesn't make sense
    public void NegativeLimit() {
	sensor.overrideSpeedLimit(-20);
	verify(user).setAlarmState(true);
    }

    @Test //the speed limit drops to 60, while train moves at 200
    public void TooMuchDifference() {
	when(controller.getReferenceSpeed()).thenReturn(200);
	sensor.overrideSpeedLimit(60);
	verify(user).setAlarmState(true);
    }
}
