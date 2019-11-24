package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Ron on 11/16/2016.
 * Modified: 11/18/2019
 *
 * 11-21-2019: Updated to fit LikeClockwork's Autonomous plan
 *
 package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Ron on 11/16/2016.
 * Modified: 11/18/2019
 * 11-21-2019: Updated to fit LikeClockwork's Autonomous plan
 * 11-23-2019: Additional updates but not functional - periodically
 *             app crashes when attempting to use this init menu
 * <p>
 * This class provides configuration for an autonomous opMode.
 * Most games benefit from autonomous opModes that can implement
 * different behavior based on an alliance strategy agreed upon
 * for a specific match.
 * </p>
 * <p>
 * Creating multiple opModes to meet this requirement results in duplicate
 * code and an environment that makes it too easy for a driver to
 * choose the wrong opMode "in the heat of battle."
 * </p>
 * <p>
 *     This class is a way to solve these problems.
 * </p>
 */

public class AutonomousConfiguration {
    /*
     Pass in gamepad and telemetry from your opMode when creating this object.
     */
    public AutonomousConfiguration(Gamepad gamepad, Telemetry telemetry1, LinearOpMode opModeObjArg) {
        this.opModeObj = opModeObjArg;
//orig:    public AutonomousConfiguration(Gamepad gamepad, Telemetry telemetry1) {
        this.gamePad1 = gamepad;
        this.telemetry = telemetry1;
        // Default selections if driver does not select anything.
        alliance = AllianceColor.None;
        startPosition = StartPosition.None;
        navigationLane = NavigationLane.None;
        reposition = Reposition.None;
        deliver = Deliver.None;
    }

    private LinearOpMode opModeObj;  // NEW for opModeIsActive()
    private AllianceColor alliance;
    private StartPosition startPosition;
    private NavigationLane navigationLane;
    private Deliver deliver;
    private Reposition reposition;
    private Gamepad gamePad1;
    private Telemetry telemetry;

    public enum AllianceColor {
        None,
        Red,
        Blue
    }

    // Where do we start the robot
    public enum StartPosition {
        None,
        BuildingZone,
        LoadingZone;

        public StartPosition getNext() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    /*
        OutSide is the lane next to the wall.
        InSide is the lane closest to the neutral sky bridge.
    */
    public enum NavigationLane {
        None,
        Inside,
        Outside;

        public NavigationLane getNext() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    /*
        Deliver means get the stone under the sky bridge.
        DeliverAndPlace means get the stone under the sky bridge
        and place it on the foundation.
     */
    public enum Deliver {
        None,
        Deliver,
        DeliverAndPlace;

        public Deliver getNext() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    /*
        Reposition the foundation. Additional values could be added if your
        robot can reposition in different locations. For example rotating the
        foundation 90 degrees.
     */
    public enum Reposition {
        None,
        No,
        Yes;

        public Reposition getNext() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    public AllianceColor getAlliance() {
        return alliance;
    }

    public StartPosition getStartPosition() {
        return startPosition;
    }

    public NavigationLane getNavigationLane() {
        return navigationLane;
    }

    public Deliver getDeliver() {
        return deliver;
    }

    public Reposition getReposition() {
        return reposition;
    }

    // Call this from your opMode to show the menu for selection.
    public void ShowMenu() {
        ElapsedTime runTime = new ElapsedTime();
        telemetry.setAutoClear(false);
        Telemetry.Item teleAlliance = telemetry.addData("Alliance: X = Blue, B = Red", getAlliance());
        Telemetry.Item teleNavigationLane = telemetry.addData("Parking lane (Dpad Up)", getNavigationLane());
        Telemetry.Item teleReposition = telemetry.addData("Park Only? (Dpad right)", getReposition());
        //Telemetry.Item teleStartPosition = telemetry.addData("Dpad left/right, select start position", getStartPosition());
        //Telemetry.Item teleDeliver = telemetry.addData("Left Bumper to cycle deliver", getDeliver());
        telemetry.addData("", "");
        telemetry.addData("When finished: ", "Press game pad Start");

        // Loop while driver makes selections.
        do {
            if (gamePad1.x) {
                alliance = AllianceColor.Blue;
                while ( gamePad1.x ) { }        // Capture long press of button
            }

            if (gamePad1.b) {
                alliance = AllianceColor.Red;
                while ( gamePad1.b ) { }
            }

            teleAlliance.setValue(alliance);

/*
            if (gamePad1.dpad_left) {
                startPosition = StartPosition.getNext();
            }

            teleStartPosition.setValue(startPosition);
*/

            if (gamePad1.dpad_up) {
                navigationLane = navigationLane.getNext();
                while ( gamePad1.dpad_up ) { }
            }

            teleNavigationLane.setValue(navigationLane);

/*
            if (gamePad1.dpad_down) {
                deliver = deliver.getNext();
            }

            teleDeliver.setValue(deliver);
*/

            if (gamePad1.dpad_right) {
                reposition = reposition.getNext();
                while ( gamePad1.dpad_right ) { }
            }

            teleReposition.setValue(reposition);

            telemetry.update();

            // If there is no gamepad timeout for debugging.
            if (false && gamePad1.id == -1) {
                // The timer is for debugging, remove it when you have a gamepad connected.
                if (runTime.seconds() > 7) {
                    break;
                }
            } else {
                // Only allow loop exit if alliance and lane have been selected.
                if (gamePad1.start && alliance != AllianceColor.None &&
                                      navigationLane != navigationLane.None ) {
                    break;
                }
            }
        //} while (true);
        } while ((runTime.seconds() < 5) || (!opModeObj.isStopRequested()));
    }
}
