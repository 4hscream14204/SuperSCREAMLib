package org.screamrobotics.SuperSCREAMLib.hardware;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.screamrobotics.SuperSCREAMLib.controller.PIDController;
import org.screamrobotics.SuperSCREAMLib.controller.PIDFController;

import java.util.ArrayList;
import java.util.List;

public class Launcher {
    public DcMotorEx launcherMotor;
    PIDController pidController;
    PIDFController pidfController;
    double targetVel;
    int velStorageSize = 3;
    List<Double> velStorage = new ArrayList<>();
    double secondDegreeCoefficient = 0;
    double firstDegreeCoefficient = 0;
    double constant = 0;

    private enum PIDType{
        NO,
        PID,
        PIDF
    }

    PIDType pidType;

    public Launcher(DcMotorEx m_launcherMotor){
        launcherMotor = m_launcherMotor;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        for (int i = 0; i < velStorageSize; i++) {
            velStorage.add(0.0);
        }
        pidType = PIDType.NO;
    }
    public Launcher(DcMotorEx m_launcherMotor, PIDController m_pid){
        launcherMotor = m_launcherMotor;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        pidController = m_pid;

        for (int i = 0; i < velStorageSize; i++) {
            velStorage.add(0.0);
        }
        pidType = PIDType.PID;
    }
    public Launcher(DcMotorEx m_launcherMotor, PIDFController m_pidf){
        launcherMotor = m_launcherMotor;
        launcherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        pidfController = m_pidf;

        for (int i = 0; i < velStorageSize; i++) {
            velStorage.add(0.0);
        }
        pidType = PIDType.PIDF;
    }

    /**
     *
     * @param m_velocity sets the motor velocity to this.
     * Depending on if you are using a PID or not, it will do things a little differently
     */

    public void setVelocity(double m_velocity) {
        if(pidType == PIDType.NO){
            launcherMotor.setVelocity(m_velocity);
        }
        if(pidType == PIDType.PID){
            launcherMotor.setPower(pidController.calculate(getVelocity(), m_velocity));
        }
        else {
            launcherMotor.setPower(pidfController.calculate(getVelocity(), m_velocity));
        }
        velStorage.add(getVelocity());
        velStorage.remove(0);
        targetVel = m_velocity;
    }

    public double getVelocity(){
        return launcherMotor.getVelocity();
    }

    /**
     *
     * @param m_secondDegreeCoefficient is the coefficient for the x^2 of your equation. Type 0 if you have no x^2 variable
     * @param m_firstDegreeCoefficient is the coefficient for the x of your equation.
     * @param m_constant is the constant of your equation
     */
    public void createDynamicVelocityEquation(double m_secondDegreeCoefficient, double m_firstDegreeCoefficient, double m_constant){
        secondDegreeCoefficient = m_secondDegreeCoefficient;
        firstDegreeCoefficient = m_firstDegreeCoefficient;
        constant = m_constant;
    }

    /**
     *
     * @param distance The units of distance must align with the units you used to create your equation
     */

    public void setDyanmicVelocity(double distance){
        double launchVelocity = ((secondDegreeCoefficient * (Math.pow(distance, 2))) + (firstDegreeCoefficient * distance) + constant);
        setVelocity(launchVelocity);
    }

    /**
     *
     * @param follower Your Pedro Pathing Follower
     * @param targetPose A Pose that represents the target you want to launch towards
     * @return A distance in inches between the robot and the Pose
     */

    public double getLaunchingDistanceIN(Follower follower, Pose targetPose){
        return follower.pose().distance(targetPose);
    }

    /**
     *
     * @param robotX Robot X Coordinate
     * @param robotY Robot Y Coordinate
     * @param targetX Target X Coordinate
     * @param targetY Target Y Coordinate
     * @return A distance in inches between the robot and the target coordinate
     */

    public double getLaunchingDistanceIN(double robotX, double robotY, double targetX, double targetY){
        return Math.sqrt( Math.pow((targetX - robotX), 2) + Math.pow((targetY - robotY), 2) );
    }

    /**
     * Calculates an average velocity to check against to make isAtSpeed() more consistent
     * @return True if the launcher is at the speed requested by setVelocity()
     */
    public boolean isAtSpeed(){
        double averageSpeed = 0;
        int velStorageIndex = 0;

        for (int i = 0; i < velStorageSize; i++) {
            averageSpeed = averageSpeed + velStorage.get(velStorageIndex);
            velStorageIndex ++;
        }

        averageSpeed = averageSpeed / velStorageSize;

        return Math.abs((averageSpeed - targetVel)) <= 10;
    }

    /**
     *
     * @param velocity The velocity it is comparing to
     * @return True if the launcher is at the speed requested by the user
     */
    public boolean isAtSpeed(double velocity){
        return Math.abs((velocity - targetVel)) <= 10;
    }

}
