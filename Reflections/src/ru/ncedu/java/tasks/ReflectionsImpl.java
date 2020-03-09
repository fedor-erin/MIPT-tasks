package ru.ncedu.java.tasks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectionsImpl implements Reflections {
	@Override
    public Object getFieldValueByName(Object object, String fieldName) throws NoSuchFieldException {
        if (fieldName == null || object == null){
            throw new NullPointerException();
        }
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }
	@Override
	public Set<String> getProtectedMethodNames(Class clazz) {
		Set<String> mySet = new HashSet<String>();
		Method[] methods = clazz.getDeclaredMethods(); 
		for (Method method : methods) {  
			if(method.getModifiers() == 4) {
				mySet.add(method.getName());
			}
		}
		return mySet;
	}
	@Override
	public Set<Method> getAllImplementedMethodsWithSupers(Class clazz) 	{
		if(clazz == null) {
			throw new NullPointerException();
		}
		Set<Method> mySet = new HashSet<Method>();
	    while (clazz != null) {
	        for (Method method : clazz.getDeclaredMethods()) {
	        	mySet.add(method);
	        }
	        clazz = clazz.getSuperclass();
	    }
		return mySet;
	}
	@Override
	public List<Class> getExtendsHierarchy(Class clazz) {
		List<Class> myList = new ArrayList<Class>();
		Class current = clazz;
		while(current.getSuperclass()!=null){
		    current = current.getSuperclass();
		    myList.add(current);
		}
		return myList;
	}
	@Override
	public Set<Class> getImplementedInterfaces(Class clazz) {
		Set<Class> mySet = new HashSet<Class>(Arrays.asList(clazz.getInterfaces()));
		return mySet;
	}
	@Override
	public List<Class> getThrownExceptions(Method method) {
		List<Class> myList = new ArrayList<Class>();
		myList.addAll(Arrays.asList(method.getExceptionTypes())); 
		return myList;
	}
	@Override
	public String getFooFunctionResultForDefaultConstructedClass() {
		Class<?> clazz;
		try {
			clazz = Class.forName("ru.ncedu.java.tasks.Reflections");
			clazz = clazz.getClasses()[0];
			
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class<?>[0]);
			constructor.setAccessible(true);
			
			Object secretClassInstance = constructor.newInstance(new Object[0]);
			
			Method method = clazz.getDeclaredMethod("foo", new Class<?>[0]);
			method.setAccessible(true);
			
			String result = (String) method.invoke(secretClassInstance, new Object[0]);
			return result;
			
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Class was not found", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Method or constructor was not found", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("Method is private", e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Constructor error", e);
		}
	}
	@Override
	public String getFooFunctionResultForClass(String constructorParameter, String string, Integer... integers) {
		Class<?> clazz;
		try {
			clazz = Class.forName("ru.ncedu.java.tasks.Reflections");
			clazz = clazz.getClasses()[0];
			
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class<?>[0]);
			constructor.setAccessible(true);
			
			Object secretClassInstance = constructor.newInstance(new Object[0]);
			
			Method method = clazz.getDeclaredMethod("foo", new Class<?>[0]);
			
			method = clazz.getDeclaredMethod("foo", new Class<?>[] {String.class, Integer[].class});
			
			method.setAccessible(true);
			
			//String result = (String) method.invoke(secretClassInstance, new Object[] {"Sum", new Integer[]{1, 2, 3, 4, 5}});
			String result = (String) method.invoke(secretClassInstance, new Object[] {string, integers});
			return result;
			
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Class was not found", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Method or constructor was not found", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("Method is private", e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Constructor error", e);
		}
	}
}