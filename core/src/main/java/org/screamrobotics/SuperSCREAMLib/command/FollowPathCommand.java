package org.screamrobotics.SuperSCREAMLib.command;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;

public class FollowPathCommand extends CommandBase {
    private final Follower follower;
    private final Path path;

    public FollowPathCommand(Follower follower, Path path) {
        this.follower = follower;
        this.path = path;
    }

    @Override
    public void initialize() {
        follower.follow(path);
    }

    @Override
    public boolean isFinished() {
        return !follower.isBusy();
    }
}
