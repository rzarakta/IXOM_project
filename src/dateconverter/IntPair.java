package dateconverter;

public class IntPair {

	int value, remainder;
	
	public IntPair(int a, int b){
		value = a;
		remainder = b;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getRemainder() {
		return remainder;
	}

	public void setRemainder(int remainder) {
		this.remainder = remainder;
	}

}
