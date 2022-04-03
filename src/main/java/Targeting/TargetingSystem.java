package Targeting;

import Kinematics.SystemDynamics;
import Utils.Angle;
import Utils.DistanceUnit;
import Utils.Vector3;

public class TargetingSystem {
    public class TargetingData{
        public Angle pitch;
        public Angle turret;
        public double shooterVel;

        @Override
        public String toString() {
            return shooterVel + ", " + pitch.degrees() + ", " + turret.degrees();
        }
    }

    private double cameraHeight, goalHeight, maxHeight, shooterHeight;
    private double minShooterVel = 0, maxShooterVel = Double.MAX_VALUE;
    private boolean targetHorizontal;

    private boolean turretFlipProtection = true;
    private DistanceUnit unit;

    private Angle minPitch = Angle.degrees(0), maxPitch = Angle.degrees(90);

    public TargetingSystem(double cameraHeight, double goalHeight, double shooterHeight, boolean targetHorizontal, DistanceUnit unit){
        this.cameraHeight = cameraHeight;
        this.goalHeight = goalHeight;
        this.targetHorizontal = targetHorizontal;
        this.maxHeight = goalHeight;
        this.unit = unit;
        this.shooterHeight = shooterHeight;
    }

    public void setMinimumPeakAltitude(double altitude){
        this.maxHeight = altitude;
    }

    public void setPitchLimits(Angle min, Angle max){
        this.minPitch = min;
        this.maxPitch = max;
    }

    public void setShooterLimits(double minVel, double maxVel){
        this.minShooterVel = minVel;
        this.maxShooterVel = maxVel;
    }

    public void disableTurretFlipProtection(){
        this.turretFlipProtection = false;
    }

    public TargetingData calculateVelocityFeedforward(SystemDynamics dynamics, double R, Angle cameraX, Angle cameraYaw){
        Vector3 staticVector = solveStaticTrajectory(R, cameraX, cameraYaw);
        Vector3 velocityVector = solveVelocityTrajectory(dynamics);

        if(staticVector == null){
            return null;
        }

        Vector3 combinedVector = staticVector.add(velocityVector.scale(-1));
        double mag = combinedVector.length();
        double planarMag = Math.sqrt((combinedVector.getA() * combinedVector.getA()) + (combinedVector.getB() * combinedVector.getB()));

        Angle pitch = Angle.radians(Math.atan2(combinedVector.getC(), planarMag));
        Angle turret = Angle.radians(Math.atan2(combinedVector.getB(), combinedVector.getA()));

        TargetingData data = new TargetingData();
        data.shooterVel = mag;
        data.pitch = pitch;
        data.turret = turret;

        /**
        if(Math.abs(getRotDist(data.turret, cameraYaw).degrees()) > 90){
            return null;
        }
         */

        if(turretFlipProtection){
            if(Math.signum(dynamics.xVel) != Math.signum(combinedVector.getA())){
                //System.out.println(dynamics.xVel + " | " + combinedVector.getA());
                if(dynamics.xVel != 0 && combinedVector.getA() != 0) {
                    //return null;
                }
            }
        }

        //System.out.println(staticVector + " | " + data.toString());
        //System.out.println(data);

        return data;
    }

    public TargetingData calculateVelocityFeedforward(SystemDynamics dynamics, Angle cameraX, Angle cameraY, Angle cameraYaw){
        double R = (goalHeight - cameraHeight) / Math.tan(cameraY.radians());
        return calculateVelocityFeedforward(dynamics, R, cameraX, cameraYaw);
    }

    private Vector3 solveStaticTrajectory(double R, Angle cameraX, Angle cameraYaw){
        double goalCentricTheta = cameraX.radians() + cameraYaw.radians();
        //System.out.println(goalCentricV[0] + " | " + goalCentricV[1]);

        double prevSign = Double.NaN;
        double min = minPitch.degrees(), max = maxPitch.degrees(), pitch = 45;

        double t = 0;

        for(double p = min; p < max; p ++){
            double vHeight = (Math.sqrt(2 * unit.fromMeters(9.8*5) * (maxHeight - shooterHeight))) * (1.0/Math.sin(Math.toRadians(p)));
            double vproj = ((Math.sqrt(unit.fromMeters(9.8*5)) * R * (1.0/Math.cos(Math.toRadians(p))))/Math.sqrt(2 * R * Math.tan(Math.toRadians(p)) - 2 * (goalHeight - shooterHeight)));
            double turretTheta = 0;

            t = R / (((vproj * Math.cos(turretTheta)) * Math.cos(Math.toRadians(p))));

            double val = (0.5 * unit.fromMeters(-9.8*5) * (t * t)) + ((vproj) * Math.sin(Math.toRadians(p)) * t) - (goalHeight - shooterHeight);

            if((maxHeight / (Math.sin(Math.toRadians(p)) * vproj)) > (t)){
                continue;
            }
            if(vproj < vHeight){
                continue;
            }
            if(vproj < minShooterVel || vproj > maxShooterVel){
                continue;
            }

            pitch = p;

            if(!Double.isNaN(prevSign)){
                if(Math.signum(val) != prevSign){
                    break;
                }
            }
            prevSign = Math.signum(val);
        }

        if(Double.isNaN(prevSign)){
            return null;
        }

        //System.out.println(vproj);

        TargetingData data = new TargetingData();
        data.pitch = Angle.degrees(pitch);
        data.shooterVel = Math.sqrt(2 * unit.fromMeters(9.8*5) * (maxHeight - shooterHeight)) * (1.0/Math.sin(Math.toRadians(pitch)));
        double v2 = ((Math.sqrt(unit.fromMeters(9.8*5)) * R * (1.0/Math.cos(data.pitch.radians())))/Math.sqrt(2 * R * Math.tan(data.pitch.radians()) - 2 * (goalHeight - shooterHeight)));
        data.shooterVel = v2;
        data.turret = Angle.radians(cameraYaw.radians());

        //System.out.println(data + " | " + v2);

        double vertSpeed = Math.sin(data.pitch.radians()) * data.shooterVel;
        double planarSpeed = Math.cos(data.pitch.radians()) * data.shooterVel;
        double forSpeed = Math.cos(data.turret.radians()) * planarSpeed;
        double horizSpeed = Math.sin(data.turret.radians()) * planarSpeed;

        return new Vector3(forSpeed, horizSpeed, vertSpeed);
    }

    private Vector3 solveVelocityTrajectory(SystemDynamics dynamics){
        return new Vector3(-dynamics.xVel, -dynamics.yVel, 0);
    }

    private double getRadRotDist(double start, double end){
        double diff = (end - start + Math.PI) % (2 * Math.PI) - Math.PI;
        return diff < -Math.PI ? (diff + (Math.PI * 2)) : diff;
    }

    private Angle getRotDist(Angle start, Angle end){
        return Angle.radians(getRadRotDist(start.radians(), end.radians()));
    }

}
