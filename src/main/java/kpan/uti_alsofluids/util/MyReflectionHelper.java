package kpan.uti_alsofluids.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("deprecation")
public class MyReflectionHelper {

	@SuppressWarnings("unchecked")
	public static <T, E> T getPublicField(E instance, String fieldName) throws UnableToAccessFieldException {
		try {
			Field field;
			field = instance.getClass().getField(fieldName);
			return (T) field.get(instance);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new UnableToAccessFieldException(e);
		}
	}
	public static <T, E> void setPublicField(T instance, String fieldName, E value) throws UnableToAccessFieldException {
		try {
			Field field;
			field = instance.getClass().getField(fieldName);
			field.set(instance, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new UnableToAccessFieldException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, E> T invokePublicMethod(E instance, String methodName, Object... args) {
		Class<?>[] types = fromArgs(args);
		try {
			Method method;
			method = instance.getClass().getMethod(methodName, types);
			method.setAccessible(true);
			return (T) method.invoke(instance, args);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnableToInvokeException(e);
		}
	}

	//インライン展開されるfinal(プリミティブや文字列)は変更しても無駄

	@SuppressWarnings("unchecked")
	public static <T, E> T getPrivateField(@Nonnull E instance, String fieldName) throws UnableToAccessFieldException {
		return getPrivateFieldInternal((Class<? super E>) instance.getClass(), instance, fieldName);
	}
	public static <T, E> T getPrivateStaticField(Class<? super E> classToAccess, String fieldName) throws UnableToAccessFieldException {
		return getPrivateFieldInternal(classToAccess, null, fieldName);
	}
	public static <T, E> T getPrivateField(Class<? super E> classToAccess, @Nonnull E instance, String fieldName) throws UnableToAccessFieldException {
		return getPrivateFieldInternal(classToAccess, instance, fieldName);
	}
	//"a.b.c.Class"
	public static <T, E> T getPrivateField(String classToAccess, @Nonnull E instance, String fieldName) throws UnableToAccessFieldException {
		try {
			@SuppressWarnings("unchecked")
			Class<? super E> clazz = (Class<? super E>) Class.forName(classToAccess);
			return getPrivateField(clazz, instance, fieldName);
		} catch (ClassNotFoundException e) {
			throw new UnableToAccessFieldException(e);
		}
	}
	public static <T, E> T getPrivateStaticField(String classToAccess, String fieldName) throws UnableToAccessFieldException {
		try {
			@SuppressWarnings("unchecked")
			Class<? super E> clazz = (Class<? super E>) Class.forName(classToAccess);
			return getPrivateStaticField(clazz, fieldName);
		} catch (ClassNotFoundException e) {
			throw new UnableToAccessFieldException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, E> void setPrivateField(@Nonnull T instance, String fieldName, E value) throws UnableToAccessFieldException {
		setPrivateFieldInternal((Class<? super T>) instance.getClass(), instance, fieldName, value);
	}
	public static <T, E> void setPrivateStaticField(Class<? super T> classToAccess, String fieldName, E value) throws UnableToAccessFieldException {
		setPrivateFieldInternal(classToAccess, null, fieldName, value);
	}
	public static <T, E> void setPrivateField(Class<? super T> classToAccess, @Nonnull T instance, String fieldName, E value) throws UnableToAccessFieldException {
		setPrivateFieldInternal(classToAccess, instance, fieldName, value);
	}
	public static <T, E> void setPrivateField(String classToAccess, @Nonnull T instance, String fieldName, E value) throws UnableToAccessFieldException {
		try {
			@SuppressWarnings("unchecked")
			Class<? super T> clazz = (Class<? super T>) Class.forName(classToAccess);
			setPrivateField(clazz, instance, fieldName, value);
		} catch (ClassNotFoundException e) {
			throw new UnableToAccessFieldException(e);
		}
	}
	public static <T, E> void setPrivateStaticField(String classToAccess, String fieldName, E value) throws UnableToAccessFieldException {
		try {
			@SuppressWarnings("unchecked")
			Class<? super T> clazz = (Class<? super T>) Class.forName(classToAccess);
			setPrivateStaticField(clazz, fieldName, value);
		} catch (ClassNotFoundException e) {
			throw new UnableToAccessFieldException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, E> T invokePrivateMethod(@Nonnull E instance, String methodName, Class<?>[] parameterTypes, Object[] args) {
		return invokePrivateMethodInternal((Class<? super E>) instance.getClass(), instance, methodName, parameterTypes, args);
	}
	public static <T, E> T invokePrivateStaticMethod(Class<? super E> classToAccess, String methodName, Class<?>[] parameterTypes, Object[] args) {
		return invokePrivateMethodInternal(classToAccess, null, methodName, parameterTypes, args);
	}
	@SuppressWarnings("unchecked")
	public static <T, E> T invokePrivateMethod(@Nonnull E instance, String methodName, Object... args) {
		return invokePrivateMethodInternal((Class<? super E>) instance.getClass(), instance, methodName, args);
	}
	public static <T, E> T invokePrivateStaticMethod(Class<? super E> classToAccess, String methodName, Object... args) {
		return invokePrivateMethodInternal(classToAccess, null, methodName, args);
	}
	public static <T, E> T invokePrivateMethod(Class<? super E> classToAccess, @Nonnull E instance, String methodName, Class<?>[] parameterTypes, Object[] args) {
		return invokePrivateMethodInternal(classToAccess, instance, methodName, parameterTypes, args);
	}
	public static <T, E> T invokePrivateMethod(Class<? super E> classToAccess, @Nonnull E instance, String methodName, Object... args) {
		return invokePrivateMethodInternal(classToAccess, instance, methodName, args);
	}

	@SuppressWarnings("unchecked")
	private static <T, E> T getPrivateFieldInternal(Class<? super E> classToAccess, @Nullable E instance, String fieldName) throws UnableToAccessFieldException {
		try {
			Field field;
			field = classToAccess.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(instance);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new UnableToAccessFieldException(e);
		}
	}
	private static <T, E> void setPrivateFieldInternal(Class<? super T> classToAccess, @Nullable T instance, String fieldName, E value) throws UnableToAccessFieldException {
		try {
			Field field;
			field = classToAccess.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new UnableToAccessFieldException(e);
		}
	}
	private static <T, E> T invokePrivateMethodInternal(Class<? super E> classToAccess, @Nullable E instance, String methodName, Object... args) {
		Class<?>[] types = fromArgs(args);
		return invokePrivateMethodInternal(classToAccess, instance, methodName, types, args);
	}
	@SuppressWarnings("unchecked")
	private static <T, E> T invokePrivateMethodInternal(Class<? super E> classToAccess, @Nullable E instance, String methodName, Class<?>[] parameterTypes, Object[] args) {
		try {
			Method method;
			method = classToAccess.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return (T) method.invoke(instance, args);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnableToInvokeException(e);
		}
	}

	//マイクラ限定Obfuscated対応のリフレクション

	@SuppressWarnings("unchecked")
	public static <T, E> T getObfPrivateValue(E instance, String fieldName, @Nullable String fieldSrgName) throws UnableToAccessFieldException {
		return getObfPrivateValue((Class<? super E>) instance.getClass(), instance, fieldName, fieldSrgName);
	}
	public static <T, E> T getObfPrivateValue(Class<? super E> classToAccess, @Nullable E instance, String fieldName, @Nullable String fieldSrgName) throws UnableToAccessFieldException {
		return ReflectionHelper.getPrivateValue(classToAccess, instance, fieldName, fieldSrgName);
	}
	public static <T, E> void setObfPrivateValue(Class<? super T> classToAccess, @Nullable T instance, @Nullable E value, String fieldName, @Nullable String fieldSrgName)
			throws UnableToAccessFieldException {
		ReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldName, fieldSrgName);
	}

	@SuppressWarnings("unchecked")
	public static <T, E> T invokeObfPrivateMethod(E instance, String methodName, @Nullable String methodSrgName, Object... args) throws UnableToInvokeException {
		return invokeObfPrivateMethod((Class<? super E>) instance.getClass(), instance, methodName, methodSrgName, args);
	}
	public static <T, E> T invokeObfPrivateMethod(Class<? super E> classToAccess, @Nullable E instance, String methodName, @Nullable String methodSrgName, Object... args)
			throws UnableToInvokeException {
		return invokeObfPrivateMethod(classToAccess, instance, methodName, methodSrgName, fromArgs(args), args);
	}
	@SuppressWarnings("unchecked")
	public static <T, E> T invokeObfPrivateMethod(Class<? super E> classToAccess, @Nullable E instance, String methodName, @Nullable String methodSrgName, Class<?>[] parameterTypes, Object[] args)
			throws UnableToInvokeException {
		Method method = ReflectionHelper.findMethod(classToAccess, methodName, methodSrgName, parameterTypes);
		try {
			return (T) method.invoke(instance, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnableToInvokeException(e);
		}
	}

	private static Class<?>[] fromArgs(Object... args) {
		Class<?>[] types = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			Class<? extends Object> type = arg.getClass();
			if (type == Boolean.class)
				type = boolean.class;
			else if (type == Integer.class)
				type = int.class;
			else if (type == Short.class)
				type = short.class;
			else if (type == Character.class)
				type = char.class;
			else if (type == Byte.class)
				type = byte.class;
			else if (type == Long.class)
				type = long.class;
			else if (type == Float.class)
				type = float.class;
			else if (type == Double.class)
				type = double.class;
			types[i] = type;
		}
		return types;
	}

	public static class UnableToAccessFieldException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public UnableToAccessFieldException(Exception e) { super(e); }
	}

	public static class UnableToInvokeException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public UnableToInvokeException(Exception e) { super(e); }
	}
}
