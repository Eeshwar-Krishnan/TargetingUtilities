package Kinematics;

public class TankKinematics {
    public static SystemDynamics calculateSystemDynamics(double leftSideVel, double rightSideVel){
        SystemDynamics dynamics = new SystemDynamics();
        dynamics.xVel = (leftSideVel + rightSideVel)/2.0;
        return dynamics;
    }
}
