package com.example.ftclibexamples;

import org.screamrobotics.SuperSCREAMLib.command.CommandOpMode;
import org.screamrobotics.SuperSCREAMLib.vision.UGRectDetector;

public class UGRectRingVisionSample extends CommandOpMode {
    UGRectDetector UGRectDetector;

    @Override
    public void initialize() {
        UGRectDetector = new UGRectDetector(hardwareMap);
        UGRectDetector.init();
    }

    @Override
    public void run() {
        UGRectDetector.Stack stack = UGRectDetector.getStack();

        switch (stack) {
            case ZERO:
                break;
            case ONE:
                break;
            case FOUR:
                break;
            default:
                break;
        }
    }
}
