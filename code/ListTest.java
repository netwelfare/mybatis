package code;

import java.util.ArrayList;
import java.util.List;

public class ListTest
{

	public static void main(String[] args)
	{
		int count = 100;
		long begin = 0;
		long end = 0;
		long elapse = 0;
		List<String> list1 = new ArrayList<String>();
		list1.add(null);
		list1.add(null);
		/*begin = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			list1.add("name");
		}
		end = System.currentTimeMillis();
		elapse = end - begin;
		System.out.println("插入" + count + "条数据所需要的时间为:" + elapse);

		begin = System.currentTimeMillis();
		for (int i = 1; i <= count; i++)
		{
			list1.remove(0);
		}
		end = System.currentTimeMillis();
		elapse = end - begin;
		System.out.println("删除" + count + "条数据所需要的时间为:" + elapse);

		List<String> list2 = new LinkedList<String>();

		begin = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			list2.add("name");
		}
		end = System.currentTimeMillis();
		elapse = end - begin;
		System.out.println("插入" + count + "条数据所需要的时间为:" + elapse);

		begin = System.currentTimeMillis();
		for (int i = 1; i <= count; i++)
		{
			list2.remove(0);
		}
		end = System.currentTimeMillis();
		elapse = end - begin;
		System.out.println("删除" + count + "条数据所需要的时间为:" + elapse);*/

	}

}
