package ui;

public class Test {
	private int a;
	private int b;
	
	public Test() {
		// empty constructor
	}
	
	public int getA() { return a; }
	public int getB() { return b; }
	
	public void setA(int value) { a = value; }
	public void setB(int value) { b = value; }
	
	public int add() {
		return a*b;
	}
}