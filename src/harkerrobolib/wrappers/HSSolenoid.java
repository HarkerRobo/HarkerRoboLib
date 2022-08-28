package harkerrobolib.wrappers;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class HSSolenoid extends DoubleSolenoid {

  public HSSolenoid(PneumaticsModuleType moduleType, int forwardChannel, int reverseChannel) {
    super(moduleType, forwardChannel, reverseChannel);
  }

  @Override
  /**
   * Toggle the value of the solenoid.
   *
   * <p>If the solenoid is set to forward, it'll be set to reverse. Otherwise, it'll be set to
   * forward.
   */
  public void toggle() {
    if (get() == Value.kOff) set(Value.kForward);
    else super.toggle();
  }
}
