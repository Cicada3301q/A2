/*==========================================================================
File: ObjectInspector.java
Purpose:Demo Object inspector for the Asst2TestDriver

Location: University of Calgary, Alberta, Canada
Created By: Jordan Kidney
Created on:  Oct 23, 2005
Last Updated: Oct 23, 2005

***********************************************************************
If you are going to reproduce this code in any way for your asignment 
rember to include my name at the top of the file toindicate where you
got the original code from
***********************************************************************


========================================================================*/

import java.util.*;
import java.lang.reflect.*;

public class ObjectInspector {
	public ObjectInspector() {
	}

	// -----------------------------------------------------------
	public void inspect(Object obj, boolean recursive) {
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass();

		System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

		// inspect the current class
		inspectFields(obj, ObjClass, objectsToInspect);

		if (recursive)
			inspectFieldClasses(obj, ObjClass, objectsToInspect, recursive);

	}

	// -----------------------------------------------------------
	private void inspectFieldClasses(Object obj, Class ObjClass,
			Vector objectsToInspect, boolean recursive) {

		if (objectsToInspect.size() > 0)
			System.out.println("---- Inspecting Field Classes ----");

		Enumeration e = objectsToInspect.elements();
		while (e.hasMoreElements()) {
			Field f = (Field) e.nextElement();
			System.out.println("Inspecting Field: " + f.getName());

			try {
				System.out.println("******************");
				inspect(f.get(obj), recursive);
				System.out.println("******************");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}

	// -----------------------------------------------------------
	private void inspectFields(Object obj, Class ObjClass, Vector objectsToInspect)

	{

		Field[] fields = ObjClass.getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Class<?> fieldType = field.getType();
			int modifiers = field.getModifiers();

			System.out.println("Field Name: " + fieldName);
			System.out.println("Field Type: " + fieldType.getName());
			System.out.println("Modifiers: " + Modifier.toString(modifiers));

			try {
				Object fieldValue = field.get(obj);
				System.out.println("Field Value: " + fieldValue);

				if (fieldValue != null && !fieldType.isPrimitive()) {
					objectsToInspect.addElement(fieldValue);
				}
			} catch (Exception e) {
				// Handle exceptions appropriately, e.g., log or throw
			}
		}

		if (ObjClass.getSuperclass() != null) {
			inspectFields(obj, ObjClass.getSuperclass(), objectsToInspect);
		}
	}
}
