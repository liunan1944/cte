package com.cte.credit.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertySetStrategy;

import com.cte.credit.common.domain.BaseDomain;

/**
 * @Title: JSON工具类
 * @Package com.bill99.ifs.crs.util
 * @Description: JSON处理
 * @author liunan1944@163.com
 * @date 2017年12月8日 下午4:18:50
 * @version V1.0
 */
public class JSONUtil {

	/**
	 * JSONObject转换成对象
	 * 
	 * @param jsonObject
	 * @param classs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseDomain> T convertJson2Object(JSONObject jsonObject, Class<T> classs) {
		if (jsonObject == null || jsonObject.isNullObject())
			return null;
		// 声明JsonConfig对象
		JsonConfig cfg = new JsonConfig();
		// 设置属性包装器
		cfg.setPropertySetStrategy(new PropertyStrategyWrapper(PropertySetStrategy.DEFAULT));
		// 设置要转换成的JavaBean
		cfg.setRootClass(classs);
		// 转换
		return (T) JSONObject.toBean(jsonObject, cfg);
	}

	/**
	 * JSONArray转换成对象
	 * 
	 * @param jsonArray
	 * @param classs
	 * @return
	 */
	public static <T extends BaseDomain> List<T> convertJson2Object(JSONArray jsonArray, Class<T> classs) {
		List<T> beanList = new ArrayList<T>();
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.size(); i++) {
				// 转换
				if (!jsonArray.getJSONObject(i).isNullObject())
					beanList.add((T) convertJson2Object(jsonArray.getJSONObject(i), classs));
			}
		}
		return beanList;
	}

	/**
	 * @param jo
	 *            JSONObject
	 * @param key
	 *            key
	 * @param isJSONArray
	 *            是否获取JSONObject中的子JSONArray
	 */
	@SuppressWarnings("unchecked")
	public static Object getJsonValueByKey(JSONObject jo, String key, boolean isJSONArray) {
		if (jo == null || jo.isNullObject())
			return null;
		Object value = null;
		try {
			for (Iterator<String> keys = jo.keys(); keys.hasNext();) {
				String key1 = keys.next();
				if (key1.equals(key)) {
					// logger.debug("key1---" + key1 + "------" + jo.get(key1));
					if (!isJSONArray) {
						if (jo.get(key1) instanceof JSONObject) {
							value = (JSONObject) jo.get(key1);
						} else if (jo.get(key1) == null || !(jo.get(key1) instanceof JSONNull)) {
							value = jo.get(key1);
						}
					} else {
						if (jo.get(key1) instanceof JSONArray) {
							value = (JSONArray) jo.get(key1);
						}
					}
					break;
				} else {
					if (jo.get(key1) instanceof JSONObject) {
						value = getJsonValueByKey((JSONObject) jo.get(key1), key, isJSONArray);
						continue;
					}
				}
				// logger.debug("key1:" + key1 + "----------jo.get(key1):" +
				// jo.get(key1));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends BaseDomain> void JsonObject2TreeMap(JSONObject jo, TreeMap<String, Object> treeMap,
			Map classmap) {
		for (Iterator<String> keys = jo.keys(); keys.hasNext();) {
			try {
				String key = keys.next();
				if (jo.get(key) instanceof JSONObject) {
					if (hasJSONObjectChild((JSONObject) jo.get(key))) {
						treeMap.put(key, new TreeMap<String, Object>());
						JsonObject2TreeMap((JSONObject) jo.get(key), (TreeMap<String, Object>) treeMap.get(key),
								classmap);
					} else {
						// logger.debug("===key:" + key +
						// "   classmap.get(key):" + classmap.get(key));
						if (classmap.get(key) != null)
							treeMap.put(key, convertJson2Object((JSONObject) jo.get(key), (Class<T>) classmap.get(key)));
					}
					continue;
				}
				if (jo.get(key) instanceof JSONArray) {
					if (hasJSONObjectChild((JSONArray) jo.get(key))) {
						JSONArray joArr = (JSONArray) jo.get(key);
						List<TreeMap<String, Object>> treeList = new ArrayList<TreeMap<String, Object>>();
						for (int i = 0; i < joArr.size(); i++) {
							TreeMap<String, Object> treeMapi = new TreeMap<String, Object>();
							// logger.debug("===key11==:" + key);
							if (joArr.get(i) instanceof JSONObject) {
								JsonObject2TreeMap((JSONObject) joArr.get(i), (TreeMap<String, Object>) treeMapi,
										classmap);
							}
							if (joArr.get(i) instanceof JSONArray) {
								JsonArray2TreeMap((JSONArray) joArr.get(i), (TreeMap<String, Object>) treeMapi,
										classmap);
							}
							treeList.add(treeMapi);
						}
						treeMap.put(key, treeList);
					} else {
						if (classmap.get(key) != null)
							treeMap.put(key, convertJson2Object((JSONArray) jo.get(key), (Class<T>) classmap.get(key)));
					}
					continue;
				}
				if (!(jo.get(key) instanceof JSONNull))
					treeMap.put(key, jo.get(key));
				else
					treeMap.put(key, "");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean hasJSONObjectChild(JSONObject jo) {
		boolean has = false;
		for (Iterator<String> keys = jo.keys(); keys.hasNext();) {
			String key = keys.next();
			if (jo.get(key) instanceof JSONObject) {
				has = true;
				break;
			}
			if (jo.get(key) instanceof JSONArray) {
				has = true;
				break;
			}
		}
		return has;
	}

	public static boolean hasJSONObjectChild(JSONArray joArr) {
		boolean has = false;
		for (int i = 0; i < joArr.size(); i++) {
			if (joArr.get(i) instanceof JSONObject) {

				has = hasJSONObjectChild((JSONObject) joArr.get(i));
				break;
			}
			if (joArr.get(i) instanceof JSONArray) {

				has = hasJSONObjectChild((JSONArray) joArr.get(i));
				break;
			}
		}
		return has;
	}

	@SuppressWarnings("rawtypes")
	public static void JsonArray2TreeMap(JSONArray joArr, TreeMap<String, Object> treeMap, Map classmap) {
		for (int i = 0; i < joArr.size(); i++) {
			try {
				if (joArr.get(i) instanceof JSONObject) {

					JsonObject2TreeMap((JSONObject) joArr.get(i), treeMap, classmap);
					continue;
				}
				if (joArr.get(i) instanceof JSONArray) {

					JsonArray2TreeMap((JSONArray) joArr.get(i), treeMap, classmap);
					continue;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

class PropertyStrategyWrapper extends PropertySetStrategy {
	private PropertySetStrategy original;

	public PropertyStrategyWrapper(PropertySetStrategy original) {
		this.original = original;
	}

	@Override
	public void setProperty(Object o, String string, Object o1) throws JSONException {
		try {
			original.setProperty(o, string, o1);
		} catch (Exception ex) {
			// ignore
		}
	}
}