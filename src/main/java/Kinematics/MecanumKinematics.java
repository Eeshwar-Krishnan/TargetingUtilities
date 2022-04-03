package Kinematics;

public class MecanumKinematics {
    public static SystemDynamics calculateSystemDynamics(double frontLeftWheelVel, double frontRightWheelVel, double backLeftWheelVel, double backRightWheelVel){
        SystemDynamics dynamics = new SystemDynamics();
        dynamics.xVel = (frontLeftWheelVel + frontRightWheelVel + backLeftWheelVel + backRightWheelVel)/4.0;
        dynamics.yVel = (backLeftWheelVel + frontRightWheelVel - frontLeftWheelVel - backRightWheelVel)/4.0;
        return dynamics;
    }
}
