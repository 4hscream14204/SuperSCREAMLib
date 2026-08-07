package org.screamrobotics.SuperSCREAMLib.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class RGBLight {

    Servo lightServo;

    public enum RGBLightColors{
        RED(0),
        ORANGE(0.1),
        YELLOW(0.2),
        GREEN(0.3),
        BLUE(0.4),
        PURPLE(0.5);
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
