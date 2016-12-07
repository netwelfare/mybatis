package code.net.swiftlet.log;

public class Test {
	public <T> T print(String str) {
		T t = null;
		str = t.toString();
		return t;
	}

	public <T> T println(String st) {
		Test t = new Test();
		return t.<T> print(st);
	}
}
