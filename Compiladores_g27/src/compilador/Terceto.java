package compilador;

public class Terceto {
    private String op1;
    private String op2;
    private String op3;

    public Terceto(String op1, String op2, String op3) {
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public void print() {
        System.out.println("(" + op1 + "," + op2 + "," + op3 + ")");
    }
}
