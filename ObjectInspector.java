/*==========================================================================
Some code taken from Jordan Kidney 
Author: Quenten Welch
Date: October 15th 2023

Desciption: A reflective object inspector
//step 1 create all field, method, constructor inspector methods COMPLETED
//Step 2 address superclass and interfaces
//step 3 call inspectSuite on every object discovered, traverse hierachy up to Object. 
//CHECK, do i properly handly arrays in all instances? in Method, in Field, in Constructor
========================================================================*/

import java.util.*;
import java.lang.reflect.*;

public class ObjectInspector {
	private Set<Class<?>> inspectedClasses;

	public ObjectInspector() {
		inspectedClasses = new HashSet<>();
	}

	// -----------------------------------------------------------
	public void inspect(Object obj, boolean recursive) {
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass();

		System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

		System.out.println("---- Printing Class Information ----");
		inspectClassInformation(ObjClass);

		System.out.println("---- Printing Method Information ----");
		inspectMethods(ObjClass);

		System.out.println("---- Printing Field Information ----");
		inspectFields(obj, ObjClass, objectsToInspect, inspectedClasses);

		System.out.println("---- Printing Constructor Information ----");
		inspectConstructors(ObjClass, inspectedClasses);
		inspectSuperclasses(ObjClass, objectsToInspect, recursive);
		inspectSuperinterfaces(ObjClass, objectsToInspect, recursive);

		if (recursive)
			inspectFieldClasses(obj, ObjClass, objectsToInspect, recursive);

	}

	private void inspectSuperclasses(Class<?> ObjClass, Vector<Object> objectsToInspect, boolean recursive) {
		Class<?> currentClass = ObjClass.getSuperclass();
		while (currentClass != null) {
			System.out.println("---- Inspecting Superclass: " + currentClass.getName() + " ----");
			System.out.println("---- Printing Class Information ----");
			inspectClassInformation(currentClass);

			System.out.println("---- Printing Method Information ----");
			inspectMethods(currentClass);

			System.out.println("---- Printing Field Information ----");
			inspectFields(null, currentClass, objectsToInspect, inspectedClasses);

			System.out.println("---- Printing Constructor Information ----");
			inspectConstructors(currentClass, inspectedClasses);

			if (recursive) {
				inspectFieldClasses(null, currentClass, objectsToInspect, recursive);
			}

			inspectSuperinterfaces(currentClass, objectsToInspect, recursive);

			currentClass = currentClass.getSuperclass();
		}
	}

	// Add this method to inspect superinterfaces recursively
	private void inspectSuperinterfaces(Class<?> ObjClass, Vector<Object> objectsToInspect, boolean recursive) {
		Class<?>[] interfaces = ObjClass.getInterfaces();
		for (Class<?> anInterface : interfaces) {
			if (!inspectedClasses.contains(anInterface)) {
				System.out.println("---- Inspecting Superinterface: " + anInterface.getName() + " ----");
				System.out.println("---- Printing Class Information ----");
				inspectClassInformation(anInterface);

				System.out.println("---- Printing Method Information ----");
				inspectMethods(anInterface);

				System.out.println("---- Printing Field Information ----");
				inspectFields(null, anInterface, objectsToInspect, inspectedClasses);

				System.out.println("---- Printing Constructor Information ----");
				inspectConstructors(anInterface, inspectedClasses);

				if (recursive) {
					inspectFieldClasses(null, anInterface, objectsToInspect, recursive);
				}

				inspectSuperinterfaces(anInterface, objectsToInspect, recursive);
			}
		}
	}

	private void inspectClassInformation(Class<?> ObjClass) {
		System.out.println("Declaring Class: " + ObjClass.getName());

		Class<?> superClass = ObjClass.getSuperclass();
		if (superClass != null) {
			System.out.println("Immediate Superclass: " + superClass.getName());
		} else {
			System.out.println("Immediate Superclass: None");
		}

		Class<?>[] interfaces = ObjClass.getInterfaces();
		if (interfaces.length > 0) {
			System.out.print("Implemented Interfaces: ");
			for (Class<?> anInterface : interfaces) {
				System.out.print(anInterface.getName() + " ");
			}
			System.out.println();
		} else {
			System.out.println("Implemented Interfaces: None");
		}
	}

	private void inspectMethods(Class<?> ObjClass) {
		Method[] methods = ObjClass.getDeclaredMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			Class<?>[] parameterTypes = method.getParameterTypes();
			Class<?> returnType = method.getReturnType();
			Class<?>[] exceptionTypes = method.getExceptionTypes();
			int modifiers = method.getModifiers();

			System.out.println("Method Name: " + methodName);
			System.out.println("Return Type: " + returnType.getName());
			System.out.println("Modifiers: " + Modifier.toString(modifiers));

			if (parameterTypes.length > 0) {
				System.out.print("Parameter Types: ");
				for (Class<?> parameterType : parameterTypes) {
					System.out.print(parameterType.getName() + " ");
				}
				System.out.println();
			} else {
				System.out.println("Parameter Types: None");
			}

			if (exceptionTypes.length > 0) {
				System.out.print("Exceptions Thrown: ");
				for (Class<?> exceptionType : exceptionTypes) {
					System.out.print(exceptionType.getName() + " ");
				}
				System.out.println();
			} else {
				System.out.println("Exceptions Thrown: None");
			}

			System.out.println();
		}
	}

	// -----------------------------------------------------------
	private void inspectFieldClasses(Object obj, Class ObjClass,
			Vector objectsToInspect, boolean recursive) {

		if (objectsToInspect.size() > 0)
			System.out.println("---- Inspecting Field Classes ----");

		// Enumeration e = objectsToInspect.elements();
		// while (e.hasMoreElements()) {
		// Field f = (Field) e.nextElement();
		// System.out.println("Inspecting Field: " + f.getName());

		// try {
		// System.out.println("******************");
		// inspect(f.get(obj), recursive);
		// System.out.println("******************");
		// } catch (Exception exp) {
		// exp.printStackTrace();
		// }
		// }
	}

	// -----------------------------------------------------------
	private void inspectFields(Object obj, Class<?> objClass, Vector<Object> objectsToInspect,
			Set<Class<?>> inspectedClasses) {
		Class<?> currentClass = objClass;

		Field[] fields = currentClass.getDeclaredFields();

		for (Field field : fields) {
			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				Class<?> fieldType = field.getType();
				int modifiers = field.getModifiers();

				System.out.println("Field Name: " + fieldName);
				if (fieldType.isArray()) {
					System.out.println("Field Type: " + fieldType.getComponentType().getName());
				} else {
					System.out.println("Field Type: " + fieldType.getName());
				}
				System.out.println("Modifiers: " + Modifier.toString(modifiers));

				try {
					Object fieldValue = field.get(obj);
					if (fieldValue != null && fieldType.isArray()) {
						int length = Array.getLength(fieldValue);
						System.out.println("Array Length: " + length);
						for (int i = 0; i < length; i++) {
							Object arrayElement = Array.get(fieldValue, i);
							System.out.println("Element " + i + ": " + arrayElement);
						}
					} else {
						System.out.println("Field Value: " + fieldValue);
					}

					if (fieldValue != null && !fieldType.isPrimitive()) {
						objectsToInspect.addElement(fieldValue);
					}
				} catch (Exception e) {
					// Handle exceptions when getting field value appropriately, e.g., log or throw
				}
			} catch (InaccessibleObjectException e) {
				System.out.println("Object not accessible: " + e.getMessage());
			} catch (SecurityException e) {
				System.out.println("Security exception: " + e.getMessage());
			}
		}

		currentClass = currentClass.getSuperclass();

	}

	private void inspectConstructors(Class<?> objClass, Set<Class<?>> inspectedClasses) {
		Class<?> currentClass = objClass;

		// if (inspectedClasses.contains(currentClass)) {
		// break; // Avoid re-inspecting the same class
		// }

		Constructor<?>[] constructors = currentClass.getDeclaredConstructors();

		for (Constructor<?> constructor : constructors) {
			String constructorName = constructor.getName();
			int modifiers = constructor.getModifiers();
			Class<?>[] parameterTypes = constructor.getParameterTypes();

			System.out.println("Constructor Name: " + constructorName);
			System.out.println("Modifiers: " + Modifier.toString(modifiers));

			System.out.print("Parameter Types: ");
			for (Class<?> parameterType : parameterTypes) {
				System.out.print(parameterType.getName() + " ");
			}
			System.out.println();
		}

		// inspectedClasses.add(currentClass);

	}
}
