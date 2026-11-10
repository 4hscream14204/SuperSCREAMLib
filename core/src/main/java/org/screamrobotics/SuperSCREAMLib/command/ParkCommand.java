package org.screamrobotics.SuperSCREAMLib.command;

import com.qualcomm.robotcore.hardware.DcMotor;

public class ParkCommand extends SequentialCommandGroup{
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public ParkCommand(DcMotor m_frontLeft, DcMotor m_frontRight, DcMotor m_backLeft, DcMotor m_backRight, int waitTimeMS, int powerTimeMS, double motorPower){
        frontLeft = m_frontLeft;
        frontRight = m_frontRight;
        backLeft = m_backLeft;
        backRight = m_backRight;

        addCommands(
                new WaitCommand(waitTimeMS),
                new ParallelCommandGroup(
                        new InstantCommand(()->frontLeft.setPower(motorPower)),
                        new InstantCommand(()->frontRight.setPower(motorPower)),
                        new InstantCommand(()->backLeft.setPower(motorPower)),
                        new InstantCommand(()->backRight.setPower(motorPower))
                ),
                new WaitCommand(powerTimeMS),
                new ParallelCommandGroup(
                        new InstantCommand(()->frontLeft.setPower(0)),
                        new InstantCommand(()->frontRight.setPower(0)),
                        new InstantCommand(()->backLeft.setPower(0)),
                        new InstantCommand(()->backRight.setPower(0))
                )
        );
    }
}
