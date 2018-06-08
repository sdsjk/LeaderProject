package com.seaboxdata.portal.utils;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linewell.utils.GsonUtil;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 清理转换bean对象中没有的属性
 * 
 * @author cjianyan@linewell.com
 * 
 */
public class ClearNotFieldAttrUtils {

	/**
	 * 获取需要清理、以及不用清理的属性
	 * @param cls
	 * @return
	 */
	private static String[] getFieldStrs(Class cls){
		
		StringBuilder sb = new StringBuilder();
		StringBuilder extSb = new StringBuilder();
		sb.append("");
		extSb.append("");

		
		Field f = null;
		String type = null;//得到此属性的类型
		Class<?> clas = cls;
		Field[] fs = null;
		while(clas!=Object.class) {

			fs = clas.getDeclaredFields();

			for (int i = 0; i < fs.length; i++) {
				f = fs[i];
				type = f.getType().toString();//得到此属性的类型

				if (type.endsWith("Date") || type.endsWith("Calendar")) {
					extSb.append("[").append(f.getName()).append("]");
					continue;
				}

				sb.append("[").append(f.getName()).append("]");

			}
			clas = clas.getSuperclass();
		}

		return new String[]{sb.toString(),extSb.toString()};
	}
	
	
	/**
	 * 清理字符串的json格式数据
	 * @param jsonObject			json字符串
	 * @param cls					要转换的对象
	 * @param subMap				要转换的子对象
	 * @return
	 */
	public static void clear(JsonObject jsonObject ,Class cls,Map<String,Class> subMap){
		
		if(jsonObject == null){
			return ;
		}
		
		String[] str =getFieldStrs(cls);
		Iterator<Map.Entry<String,JsonElement>> it = jsonObject.entrySet().iterator();
		Map.Entry<String,JsonElement> entry = null;
		while(it.hasNext()){
			
			entry = it.next();
			
			if(str[1].contains("["+entry.getKey()+"]")){
				it.remove();
				continue;
			}
			
			if(str[0].contains("["+entry.getKey()+"]")){
				
				if(subMap.containsKey(entry.getKey()) && entry.getValue()!=null ){
					if(entry.getValue().isJsonObject()){
						clear(entry.getValue().getAsJsonObject(), subMap.get(entry.getKey()));
					}else if(entry.getValue().isJsonArray()){
						JsonArray jsonArray = entry.getValue().getAsJsonArray();
						for(int i =0;i<jsonArray.size();i++){
							if(jsonArray.get(i).isJsonObject()){
								clear(jsonArray.get(i).getAsJsonObject(), subMap.get(entry.getKey()));
							}
						}
					}
				}
				
				continue;
			}
			
			it.remove();		
		}
		
	}
	
	/**
	 * 清理字符串的json格式数据
	 * @param jsonStr				json字符串
	 * @param cls					要转换的对象
	 * @param subMap				要转换的子对象
	 * @return
	 */
	public static String clear(String jsonStr ,Class cls,Map<String,Class> subMap){
		
		if(subMap ==null || subMap.size()==0){
			return clear(jsonStr, cls);
		}
		
		JsonObject jsonObject = GsonUtil.getJsonObject(jsonStr);
		
		clear(jsonObject, cls, subMap);
		
		return jsonObject.toString();
	}
	
	/**
	 * 清理不存在的对象
	 * @param jsonObject		json对象		
	 * @param cls
	 * @return
	 */
	public static void clear(JsonObject jsonObject, Class cls) {
		
		if(jsonObject == null){
			return ;
		}
		
		String[] strs =getFieldStrs(cls);
		Iterator<Map.Entry<String,JsonElement>> it = jsonObject.entrySet().iterator();
		Map.Entry<String,JsonElement> entry = null;
		
		while(it.hasNext()){
			entry = it.next();
			if(strs[1].contains("["+entry.getKey()+"]")){
				it.remove();
				continue;
			}
			if(strs[0].contains("["+entry.getKey()+"]")){
				continue;
			}
			it.remove();		
		}
		
	}
	
	/**
	 * 清理不存在的对象
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static String clear(String jsonStr, Class cls) {
		
		if(TextUtils.isEmpty(jsonStr)){
			return jsonStr;
		}
		
		JsonObject jsonObject = GsonUtil.getJsonObject(jsonStr);
		String[] strs = getFieldStrs(cls);
		Iterator<Map.Entry<String,JsonElement>> it = jsonObject.entrySet().iterator();
		Map.Entry<String,JsonElement> entry = null;
		while(it.hasNext()){
			entry = it.next();
			if(strs[1].contains("["+entry.getKey()+"]")){
				it.remove();
				continue;
			}
			if(strs[0].contains("["+entry.getKey()+"]")){
				continue;
			}
			it.remove();
		}
		
		return jsonObject.toString();
	}
	
	class SubTest{
		
		private String subTest;

		public String getSubTest() {
			return subTest;
		}

		public void setSubTest(String subTest) {
			this.subTest = subTest;
		}
	}
	
	class Test{
		
		private String id;
		private String name;
		private Date time;
		
		private SubTest subTest;
		
		private List<SubTest> list;
		
		private Map<String,SubTest> map ;
		
		public Map<String, SubTest> getMap() {
			return map;
		}
		public void setMap(Map<String, SubTest> map) {
			this.map = map;
		}
		public List<SubTest> getList() {
			return list;
		}
		public void setList(List<SubTest> list) {
			this.list = list;
		}
		public SubTest getSubTest() {
			return subTest;
		}
		public void setSubTest(SubTest subTest) {
			this.subTest = subTest;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
		private Test test;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Test getTest() {
			return test;
		}
		public void setTest(Test test) {
			this.test = test;
		}
	}
	
	public static void main(String[] arg){
		String jsonStr ="{\"id\":\"123\",\"subTest\":{\"sss\":2,\"subTest\":\"ssss\"},\"list\":[{\"sss\":2,\"subTest\":\"ssss\"},{\"sss\":2,\"subTest\":\"ssss\"}],\"name\":\"name\",\"time\":23432432432432,\"naeme\":\"neame\",\"test\":{\"id\":\"123\",\"name\":\"name\",\"naeme\":\"neame\"}}";
		System.out.println(jsonStr);
		//System.out.println(ClearNotFieldAttrUtils.clear(GsonUtil.getJsonObject(jsonStr), Test.class));
		Map<String,Class> subMap = new HashMap<String,Class>();
		subMap.put("subTest", SubTest.class);
		subMap.put("list", SubTest.class);
		JsonObject jsonObject = GsonUtil.getJsonObject(jsonStr);
		ClearNotFieldAttrUtils.clear(jsonObject, Test.class,subMap);
		System.out.println(jsonObject.toString());
	}
}
