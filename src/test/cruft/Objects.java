package com.chalcodes.event.test;


public class Objects {
	/**
	 * Tests whether two references are either both null, or refer to equal
	 * objects.
	 *
	 * @param obj0 the first reference
	 * @param obj1 the second reference
	 * @return true if obj0 and obj1 are equal; otherwise false
	 */
	private static boolean areEqual(Object obj0, Object obj1) {
		return obj0 == null ? obj1 == null : obj0.equals(obj1);
	}
}
