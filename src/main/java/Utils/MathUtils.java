package Utils;

/**
 * Contains some commonly used math functions, so they don't need to be repeatedly typed out
 */

public class MathUtils {
    public static boolean epsilonEquals(double val1, double val2){
        return Math.abs(val1 - val2) < 1e-6;
    }

    public static double getRadRotDist(double start, double end){
        double diff = (end - start + Math.PI) % (2 * Math.PI) - Math.PI;
        return diff < -Math.PI ? (diff + (Math.PI * 2)) : diff;
    }

    public static Angle getRotDist(Angle start, Angle end){
        return Angle.radians(MathUtils.getRadRotDist(start.radians(), end.radians()));
    }

    public static double sign(double in){
        if(epsilonEquals(in, 0.0)) {
            return 0;
        }
        return in/Math.abs(in);
    }

    public static double signedMax(double val, double compare){
        if(Math.abs(val) < Math.abs(compare)){
            return sign(val) * compare;
        }
        return val;
    }

    public static double signedMin(double val, double compare){
        if(Math.abs(val) > Math.abs(compare)){
            return sign(val) * compare;
        }
        return val;
    }

    public static double clamp(double val, double min, double max){
        return Math.max(min, Math.min(max, val));
    }

    public static double millisToSec(long time){
        return (time/1000.0);
    }

    public static long secToMillis(long time){
        return time * 1000;
    }

    public static long secToNano(long time){
        return time * ((long)1_000_000_000);
    }

    public static long millisToNano(long time){
        return secToNano((long)millisToSec(time));
    }

    public static long nanoToMillis(long time){
        return secToMillis(nanoToSec(time));
    }

    public static long nanoToSec(long time){
        return time/((long)1_000_000_000);
    }

    public static double nanoToDSec(long time){
        return time/1_000_000_000.0;
    }
}
