package annotations;

public class WeakCache {
	@Cache
	public void putObject(Object key, Object value) {
		System.out.println("key:"+key.toString()+",value:"+value.toString());
	}
}
