package commandEngine.commands;

import util.annotations.Tags;
import graphics.avatar.IAvatar;

@Tags({ "Rotate Left Arm Command" })
public class RotateLeftArmCommand implements IRotateLeftArmCommand {
	private IAvatar avatar;
	private int steps;

	private final static int DELAY = 50;
	
	public RotateLeftArmCommand(IAvatar avatar, int steps) {
		this.avatar = avatar;
		this.steps = steps;
	}

	@Override
	public void run() {
		int dir = steps > 0 ? 1 : steps < 0 ? -1 : 0;
		steps = Math.abs(steps);
		for(int i = 0; i < steps; i ++) {
			avatar.rotateLeftArm(dir);
			sleep(DELAY);
		}
	}

	private static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}