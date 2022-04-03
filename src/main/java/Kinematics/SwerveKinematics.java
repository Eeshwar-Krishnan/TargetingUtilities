package Kinematics;

import Utils.Angle;

public class SwerveKinematics {

    public class SwervePod{
        public double wheelVel;
        public Angle wheelAngle;
    }

    public static SystemDynamics calculateSystemDynamics(SwervePod... pods){
        SystemDynamics dynamics = new SystemDynamics();
        for(SwervePod pod : pods){
            dynamics.xVel += (pod.wheelVel * Math.cos(pod.wheelAngle.radians())) / pods.length;
            dynamics.yVel += (pod.wheelVel * Math.cos(pod.wheelAngle.radians())) / pods.length;
        }
        return dynamics;
    }
}
