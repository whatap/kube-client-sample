package whatap.util;

public class MathUtil {
    public static double round(double v) {
        return (long) v;
    }

    public static double round(double value, int scale) {
        if (scale == 0) {
            return (double) ((long) value);
        }
        long r = scale(scale);
        long v = (long) (value * r);
        return ((double) v) / r;
    }

    public static long scale(int n) {
        long r = 0;
        switch (n) {
            case 1:
                r = 10;
                break;
            case 2:
                r = 100;
                break;
            case 3:
                r = 1000;
                break;
            default:
                r = 10000;
                break;
        }
        return r;
    }

    public static String roundString(double value, int scale) {
        if (scale <= 0) {
            return Long.toString((long) value);
        }
        return Double.toString(round(value, scale));
    }

    public static double round2(double value) {
        return round(value, 2);
    }

    public static double round4(double value) {
        return round(value, 4);
    }

	public static double getStandardDeviation(int count, double timeSum, double timeSqrSum ) {
		if (count == 0) {
			return 0;
		}
		if(timeSqrSum == 0){
			return 0;
		}
		//제곱의평균 - 평균의제곱
		double avg = timeSum/ count;
		double variation = (timeSqrSum/count) - (avg * avg);
		double ret = Math.sqrt(Math.abs(variation));
		return ret == Double.NaN ? 0 : ret;
	}

	public static double getPct90(double avg, double stdDev) {
		return (avg + stdDev * 1.282d);
	}

	public static double getPct95(double avg, double stdDev) {
		return avg+ stdDev * 1.645d;
	}
	
}
