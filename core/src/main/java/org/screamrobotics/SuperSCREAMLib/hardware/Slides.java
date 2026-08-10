package org.screamrobotics.SuperSCREAMLib.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class Slides {

    private enum MotorAmount{
        SINGLE,
        DUAL
    }

    public enum LimitSwitchUsage {
        YES_TRUE_WHEN_PRESSED,
        YES_FALSE_WHEN_PRESSED,
        NO;
    }

    LimitSwitchUsage limitSwitchUsage;
    MotorAmount motorAmount;
    DcMotorEx slideMotorL;
    DcMotorEx slideMotorR;
    DigitalChannel limitSwitch;
    boolean isStopped;
    double upPower;
    double downPower;
    int currentPos;
    double stoppingPower;

    public Slides(DcMotorEx m_slideMotor, LimitSwitchUsage m_limitSwitchUsage, DigitalChannel m_limitSwitch){
        slideMotorL = m_slideMotor;
        motorAmount = MotorAmount.SINGLE;
        limitSwitchUsage = m_limitSwitchUsage;
        limitSwitch = m_limitSwitch;

        slideMotorL.setTargetPositionTolerance(1);
        slideMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Slides(DcMotorEx m_slideMotor, DcMotorEx m_slideMotor2, LimitSwitchUsage m_limitSwitchUsage, DigitalChannel m_limitSwitch){
        slideMotorL = m_slideMotor;
        slideMotorR = m_slideMotor2;
        motorAmount = MotorAmount.DUAL;
        limitSwitchUsage = m_limitSwitchUsage;
        limitSwitch = m_limitSwitch;

        slideMotorL.setTargetPositionTolerance(1);
        slideMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotorR.setTargetPositionTolerance(1);
        slideMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Slides(DcMotorEx m_slideMotor){
        slideMotorL = m_slideMotor;
        motorAmount = MotorAmount.SINGLE;
        limitSwitchUsage = LimitSwitchUsage.NO;
        slideMotorL.setTargetPositionTolerance(1);
        slideMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Slides(DcMotorEx m_slideMotor, DcMotorEx m_slideMotor2){
        slideMotorL = m_slideMotor;
        slideMotorR = m_slideMotor2;
        motorAmount = MotorAmount.DUAL;
        limitSwitchUsage = LimitSwitchUsage.NO;
        slideMotorL.setTargetPositionTolerance(1);
        slideMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotorR.setTargetPositionTolerance(1);
        slideMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPower(double power){
        if(motorAmount == MotorAmount.SINGLE){
            slideMotorL.setPower(power);
        }
        else{
            slideMotorL.setPower(power);
            slideMotorR.setPower(power);
        }
    }

    public void setMode(DcMotor.RunMode mode){
        if(motorAmount == MotorAmount.SINGLE){
            slideMotorL.setMode(mode);
        }
        else{
            slideMotorL.setMode(mode);
            slideMotorR.setMode(mode);
        }
    }

    public void setAutomaticExtendPower(double power){
        upPower = power;
    }

    public void setAutomaticRetractPower(double power){
        downPower = power;
    }

    public void setStoppingPower(double power){
        stoppingPower = power;
    }

    private void setTargetPosition(int position){
        if(motorAmount == MotorAmount.SINGLE){
            slideMotorL.setTargetPosition(position);
        }
        else{
            slideMotorL.setTargetPosition(position);
            slideMotorR.setTargetPosition(position);
        }
    }

    public int getPosition(){
        return slideMotorL.getCurrentPosition();
    }

    public void goToPosition(int targetPosition) {
        if(getPosition() < targetPosition){
            setPower(downPower);
        }
        else if(getPosition() > targetPosition){
            setPower(upPower);
        }
        else if(isAtPosition(targetPosition)){
            stopInPlace();
            return;
        }
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setTargetPosition(targetPosition);

        isStopped = false;
    }

    public void stopInPlace(){
        if(isStopped){
            return;
        }
        isStopped = true;
        if(areSlidesHome()){
            reset();
        }
        else{
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            currentPos = getPosition();
            setTargetPosition(currentPos);
            setPower(stoppingPower);
        }
    }

    public boolean areSlidesHome(){
        if(limitSwitchUsage == LimitSwitchUsage.NO){
            return Math.abs(slideMotorL.getCurrentPosition()) <= 20;
        }
        else if(limitSwitchUsage == LimitSwitchUsage.YES_TRUE_WHEN_PRESSED){
            return limitSwitch.getState();
        }
        else{
            return !limitSwitch.getState();
        }
    }

    public boolean isAtPosition(double position){
        return Math.abs(getPosition() - position) <= 20;
    }

    public void reset(){
        isStopped = false;
        setPower(0);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPosition(0);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0);
    }

}
