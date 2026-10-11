package Numbers;

public class ComplexNumbers {
    private float real;
    private float imaginary;

    public ComplexNumbers(float real, float imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public float getRealPart() {
        return real;
    }

    public float getImaginaryPart() {
        return imaginary;
    }

    public String toString() {
        if (imaginary < 0) {
            return real + " - " + (-imaginary) + "i";
        }
        return real + " + " + imaginary + "i";
    }
}
