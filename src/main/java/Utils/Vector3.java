package Utils;

public class Vector3 {
    private static final Vector3 ZERO = new Vector3(0, 0, 0);

    private double a, b, c;
    public Vector3(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void set(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Vector3 clone(){
        return new Vector3(a, b, c);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(a-v.a, b-v.b, c-v.c);
    }

    public Vector3 add(Vector3 v){
        return new Vector3(v.a+a, v.b+b, v.c+c);
    }

    public Vector3 add(double a, double b, double c){
        return new Vector3(this.a+a, this.b+b, this.c+c);
    }

    public Vector3 scale(double scalar){
        return new Vector3(a*scalar, b*scalar, c*scalar);
    }

    public void set(Vector3 v) {
        a = v.a;
        b = v.b;
        c = v.c;
    }

    public double length(){
        return Math.sqrt((a*a) + (b*b) + (c*c));
    }

    public Vector3 normalize() {
        return scale(1/length());
    }

    public static Vector3 ZERO(){
        return ZERO.clone();
    }

    public boolean equals(Vector3 vector3){
        return (vector3.a == a) && (vector3.b == b) && (vector3.c == c);
    }

    @Override
    public String toString() {
        return a + ", " + b + ", " + c;
    }
}