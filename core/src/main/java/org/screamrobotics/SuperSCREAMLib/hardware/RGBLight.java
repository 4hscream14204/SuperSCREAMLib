package org.screamrobotics.SuperSCREAMLib.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class RGBLight {

    Servo lightServo;

    public enum RGBLightColors{
        RED(0.2777),
        ORANGE(0.3333),
        YELLOW(0.3888),
        GREEN(0.5),
        BLUE(0.6111),
        PURPLE(0.7222);
        public final double value;
        RGBLightColors(double pos){
            value = pos;
        }
    }

    public RGBLight(Servo m_lightServo){
        lightServo = m_lightServo;
    }

    public void setColor(double position){
        lightServo.setPosition(position);
    }

    public void setColor(RGBLightColors m_lightColors){
        setColor(m_lightColors.value);
    }
}
