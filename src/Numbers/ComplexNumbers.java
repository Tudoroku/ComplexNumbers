package Numbers;

public class ComplexNumbers {
    private int real;
    private int imaginary;

    public ComplexNumbers(int real, int imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public int getRealPart() {
        return real;
    }

    public int getImaginaryPart() {
        return imaginary;
    }

    public String toString() {
        if (imaginary < 0) {
            return real + " - " + (-(long)imaginary) + "i";
        }
        return real + " + " + imaginary + "i";
    }
}
