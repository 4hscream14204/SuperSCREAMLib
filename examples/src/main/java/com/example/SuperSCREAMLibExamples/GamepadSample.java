package com.example.SuperSCREAMLibExamples;

import org.screamrobotics.SuperSCREAMLib.command.CommandScheduler;
import org.screamrobotics.SuperSCREAMLib.command.InstantCommand;
import org.screamrobotics.SuperSCREAMLib.gamepad.GamepadEx;
import org.screamrobotics.SuperSCREAMLib.gamepad.GamepadKeys;
import org.screamrobotics.SuperSCREAMLib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.function.BooleanSupplier;

@TeleOp(name = "Simple Gamepad Example")
@Disabled
public class GamepadSample extends LinearOpMode {

    /**
     * We want to use the tool op gamepad to set the servo
     * to position 1 when the 'A' button is pressed, then
     * set it to position 0 when 'B' is pressed
     */
    private GamepadEx toolOp;
    private SimpleServo grip;

    @Override
    public void runOpMode() throws InterruptedException {

        toolOp = new GamepadEx(gamepad2);
        grip = new SimpleServo(hardwareMap, "gripper", 0, 270);

        // WITHOUT COMMAND BASED
        BooleanSupplier openClaw = () -> toolOp.wasJustPressed(GamepadKeys.Button.A)
                && !toolOp.isDown(GamepadKeys.Button.B);
        BooleanSupplier closeClaw = () -> !toolOp.isDown(GamepadKeys.Button.A)
                && toolOp.wasJustPressed(GamepadKeys.Button.B);
        waitForStart();
        while (opModeIsActive()) {
            toolOp.readButtons();
            if (openClaw.getAsBoolean()) {
                grip.setPosition(1);
            }
            if (closeClaw.getAsBoolean()) {
                grip.setPosition(0);
            }
        }

        // WITH COMMAND BASED
        toolOp.getGamepadButton(GamepadKeys.Button.A)
                .and(toolOp.getGamepadButton(GamepadKeys.Button.B).negate())
                .whenActive(new InstantCommand(() -> grip.setPosition(1)));
        toolOp.getGamepadButton(GamepadKeys.Button.B)
                .and(toolOp.getGamepadButton(GamepadKeys.Button.A).negate())
                .whenActive(new InstantCommand(() -> grip.setPosition(0)));
        waitForStart();
        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
        }

    }

}
