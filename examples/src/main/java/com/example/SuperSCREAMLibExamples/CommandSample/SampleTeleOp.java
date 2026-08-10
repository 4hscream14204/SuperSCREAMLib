package com.example.SuperSCREAMLibExamples.CommandSample;

import org.screamrobotics.SuperSCREAMLib.command.CommandOpMode;
import org.screamrobotics.SuperSCREAMLib.command.button.Button;
import org.screamrobotics.SuperSCREAMLib.command.button.GamepadButton;
import org.screamrobotics.SuperSCREAMLib.gamepad.GamepadEx;
import org.screamrobotics.SuperSCREAMLib.gamepad.GamepadKeys;
import org.screamrobotics.SuperSCREAMLib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Sample TeleOp")
@Disabled
public class SampleTeleOp extends CommandOpMode {

    static final double WHEEL_DIAMETER = 100.0; // millimeters

    private MotorEx m_left, m_right;
    private DriveSubsystem m_drive;
    private GamepadEx m_driverOp;
    private DefaultDrive m_driveCommand;
    private GripperSubsystem m_gripper;
    private GrabStone m_grabCommand;
    private ReleaseStone m_releaseCommand;
    private Button m_grabButton, m_releaseButton;

    @Override
    public void initialize() {
        m_left = new MotorEx(hardwareMap, "drive_left");
        m_right = new MotorEx(hardwareMap, "drive_right");
        m_drive = new DriveSubsystem(m_left, m_right, WHEEL_DIAMETER);

        m_driverOp = new GamepadEx(gamepad1);
        m_driveCommand = new DefaultDrive(m_drive, () -> m_driverOp.getLeftY(), () -> m_driverOp.getLeftX());

        m_gripper = new GripperSubsystem(hardwareMap, "gripper");
        m_grabCommand = new GrabStone(m_gripper);
        m_releaseCommand = new ReleaseStone(m_gripper);

        m_grabButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.A))
                .whenPressed(m_grabCommand);
        m_releaseButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.B))
                .whenPressed(m_releaseCommand);

        register(m_drive);
        m_drive.setDefaultCommand(m_driveCommand);
    }

}