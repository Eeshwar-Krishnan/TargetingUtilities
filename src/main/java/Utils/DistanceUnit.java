package Utils;

import java.util.Locale;

/**
 * {@link DistanceUnit} represents a unit of measure of distance.
 */
public enum DistanceUnit
    {
    METER(0), CM(1), MM(2), INCH(3), FEET(4);
    public final byte bVal;

    public static final double infinity = Double.MAX_VALUE;
    public static final double mmPerInch = 25.4;
    public static final double mPerInch = mmPerInch * 0.001;

    DistanceUnit(int i)
        {
        this.bVal = (byte)i;
        }

    //----------------------------------------------------------------------------------------------
    // Primitive operations
    //----------------------------------------------------------------------------------------------

    public double fromMeters(double meters)
        {
        if (meters==infinity) return infinity;
        switch (this)
            {
            default:
            case METER:   return meters;
            case CM:      return meters * 100;
            case MM:      return meters * 1000;
            case INCH:    return meters / mPerInch;
            case FEET: return (meters / mPerInch) / 12.0;
            }
        }

    public double fromInches(double inches)
        {
        if (inches==infinity) return infinity;
        switch (this)
            {
            default:
            case METER:   return inches * mPerInch;
            case CM:      return inches * mPerInch * 100;
            case MM:      return inches * mPerInch * 1000;
            case INCH:    return inches;
            case FEET: return inches / 12.0;
            }
        }

    public double fromCm(double cm)
        {
        if (cm==infinity) return infinity;
        switch (this)
            {
            default:
            case METER:   return cm / 100;
            case CM:      return cm;
            case MM:      return cm * 10;
            case INCH:    return fromMeters(METER.fromCm(cm));
            case FEET: return fromMeters(METER.fromCm(cm)) / 12.0;
            }
        }

    public double fromMm(double mm)
        {
        if (mm==infinity) return infinity;
        switch (this)
            {
            default:
            case METER:   return mm / 1000;
            case CM:      return mm / 10;
            case MM:      return mm;
            case INCH:    return fromMeters(METER.fromMm(mm));
            case FEET: return fromMeters(METER.fromMm(mm)) / 12.0;
            }
        }

    public double fromFeet(double feet)
        {
        return fromInches(feet * 12.0);
        }

    public double fromUnit(DistanceUnit him, double his)
        {
        switch (him)
            {
            default:
            case METER:   return this.fromMeters(his);
            case CM:      return this.fromCm(his);
            case MM:      return this.fromMm(his);
            case INCH:    return this.fromInches(his);
            case FEET:    return this.fromFeet(his);
            }
        }

    //----------------------------------------------------------------------------------------------
    // Derived operations
    //----------------------------------------------------------------------------------------------

    public double toMeters(double inOurUnits)
        {
        switch (this)
            {
            default:
            case METER:   return METER.fromMeters(inOurUnits);
            case CM:      return METER.fromCm(inOurUnits);
            case MM:      return METER.fromMm(inOurUnits);
            case INCH:    return METER.fromInches(inOurUnits);
            case FEET:    return METER.fromFeet(inOurUnits);
            }
        }

    public double toInches(double inOurUnits)
        {
        switch (this)
            {
            default:
            case METER:   return INCH.fromMeters(inOurUnits);
            case CM:      return INCH.fromCm(inOurUnits);
            case MM:      return INCH.fromMm(inOurUnits);
            case INCH:    return INCH.fromInches(inOurUnits);
            case FEET:    return INCH.fromFeet(inOurUnits);
            }
        }

    public double toCm(double inOurUnits)
        {
        switch (this)
            {
            default:
            case METER:   return CM.fromMeters(inOurUnits);
            case CM:      return CM.fromCm(inOurUnits);
            case MM:      return CM.fromMm(inOurUnits);
            case INCH:    return CM.fromInches(inOurUnits);
            case FEET:    return CM.fromFeet(inOurUnits);
            }
        }

    public double toMm(double inOurUnits)
        {
        switch (this)
            {
            default:
            case METER:   return MM.fromMeters(inOurUnits);
            case CM:      return MM.fromCm(inOurUnits);
            case MM:      return MM.fromMm(inOurUnits);
            case INCH:    return MM.fromInches(inOurUnits);
            case FEET:    return MM.fromFeet(inOurUnits);
            }
        }

        public double toFeet(double inOurUnits)
        {
            switch (this)
            {
                default:
                case METER:   return FEET.fromMeters(inOurUnits);
                case CM:      return FEET.fromCm(inOurUnits);
                case MM:      return FEET.fromMm(inOurUnits);
                case INCH:    return FEET.fromInches(inOurUnits);
                case FEET:    return FEET.fromFeet(inOurUnits);
            }
        }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    public String toString(double inOurUnits)
        {
        switch (this)
            {
            default:
            case METER:   return String.format(Locale.getDefault(), "%.3fm", inOurUnits);
            case CM:      return String.format(Locale.getDefault(), "%.1fcm", inOurUnits);
            case MM:      return String.format(Locale.getDefault(), "%.0fmm", inOurUnits);
            case INCH:    return String.format(Locale.getDefault(), "%.2fin", inOurUnits);
            case FEET:    return String.format(Locale.getDefault(), "%.2fft", inOurUnits);
            }
        }

    @Override public String toString()
        {
        switch (this)
            {
            default:
            case METER:   return "m";
            case CM:      return "cm";
            case MM:      return "mm";
            case INCH:    return "in";
            case FEET:    return "ft";
            }
        }
    }

