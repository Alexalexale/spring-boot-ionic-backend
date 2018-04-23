package com.example.main.util;

import java.lang.reflect.Field;
import java.util.Collection;

import org.modelmapper.ModelMapper;

import com.example.main.services.exceptions.MergeObjectException;

public interface ObjectUtil {

	public static <T> T fromDto(Object obj, Class<T> clazz) {
		return new ModelMapper().map(obj, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T mergeBasicData(T objModified, T objOriginal) {
		try {
			Class<?> clazz = objModified.getClass();
			Field[] fields = clazz.getDeclaredFields();
			Object returnValue = clazz.newInstance();
			for (Field field : fields) {
				if (field.getName().equals("serialVersionUID")) {
					continue;
				}
				field.setAccessible(true);
				if (Collection.class.isAssignableFrom(field.getType())) {
					field.set(returnValue, field.get(objOriginal));
				} else {
					Object value1 = field.get(objModified);
					Object value2 = field.get(objOriginal);
					Object value = (value1 != null) ? value1 : value2;
					field.set(returnValue, value);
				}
			}
			return (T) returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MergeObjectException("Erro ao fazer o merge das informações alteradas.", e);
		}
	}

}
