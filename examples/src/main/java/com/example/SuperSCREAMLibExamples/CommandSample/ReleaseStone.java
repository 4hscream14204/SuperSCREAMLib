package com.example.SuperSCREAMLibExamples.CommandSample;

import org.screamrobotics.SuperSCREAMLib.command.CommandBase;

/**
 * A simple command that releases a stone with the {@link GripperSubsystem}.  Written explicitly for
 * pedagogical purposes. Actual code should inline a command this simple with {@link
 * org.screamrobotics.SuperSCREAMLib.command.InstantCommand}.
 */
public class ReleaseStone extends CommandBase {

    // The subsystem the command runs on
    private final GripperSubsystem m_gripperSubsystem;

    public ReleaseStone(GripperSubsystem subsystem) {
        m_gripperSubsystem = subsystem;
        addRequirements(m_gripperSubsystem);
    }

    @Override
    public void initialize() {
        m_gripperSubsystem.release();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
