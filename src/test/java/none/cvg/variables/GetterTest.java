package none.cvg.variables;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

import org.junit.Test;

import static none.cvg.ErrorMessages.REFLECTION_FAILURE;
import static none.cvg.ErrorMessages.TEST_FAILURE;
import static none.cvg.ErrorMessages.UNSAFE_FAILURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*
 * TODO:
 *  This test aims at accessing public and private variables as well as a single and a
 *  multidimensional array on an existing object.
 *  Each solved test shows how this can be achieved with the traditional reflection calls.
 *  Each unsolved test provides a few hints that will allow the kata-taker to manually solve
 *  the exercise to achieve the same goal with MethodHandles/VarHandles.
 */
public class GetterTest {

    public Integer publicVariable = 1;

    private Integer privateVariable = 2;

    private int[] privatePrimitiveArrayVariable = {1, 2, 3};

    private int[][] privatePrimitive2DArrayVariable = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    /* -------------------------------------------------------------------------------------- */
    /*
     * BEGIN: PUBLIC VARIABLE USING REFLECTION AND VARIABLE HANDLES
     */
    @Test
    public void getPublicVariableFromConstructedClassTraditional() {

        try {

            Class<?> clazz = GetterTest.class;

            Field publicVariableField = clazz.getDeclaredField("publicVariable");

            assertEquals("The value of the field should be 1",
                    1,
                    publicVariableField.get(this));

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                IllegalAccessException e) {

            fail(REFLECTION_FAILURE.getValue() + e.getMessage());

        }
    }

    @Test
    public void getPublicVariableFromConstructedClassViaVarHandles() {

        try {

            /*
             * TODO:
             *  Replace the "null"s with valid values to get a VarHandle.
             *  Check API: java.lang.invoke.MethodHandles.lookup()
             *  Check API: java.lang.invoke.MethodHandles.Lookup.in(?)
             *  Check API: java.lang.invoke.MethodHandles.Lookup.findVarHandle(?, ?, ?)
             */
            VarHandle publicVariableVarHandle = MethodHandles
                    .lookup()
                    .in(null)
                    .findVarHandle(null, null, null);

            assertEquals("There should only be one coordinateType",
                    publicVariableVarHandle.coordinateTypes().size(), 1);

            assertEquals("The only coordinate type is GetterTest",
                    GetterTest.class,
                    publicVariableVarHandle.coordinateTypes().get(0));

            assertEquals("The value of the field should be 1",
                    1,
                    publicVariableVarHandle.get(this));

        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {

            fail(TEST_FAILURE.getValue() + e.getMessage());

        }

    }
    /* -------------------------------------------------------------------------------------- */

    /* -------------------------------------------------------------------------------------- */
    /*
     * BEGIN: PRIVATE VARIABLE USING REFLECTION AND VARIABLE HANDLES
     */
    @Test
    public void getPrivateVariableFromConstructedClassViaReflection() {

        try {

            Class<?> clazz = GetterTest.class;

            Field privateVariableField = clazz.getDeclaredField("privateVariable");

            privateVariableField.setAccessible(true);

            assertEquals("The value of the field should be 2",
                    2,
                    privateVariableField.get(this));

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                IllegalAccessException e) {

            fail(REFLECTION_FAILURE.getValue() + e.getMessage());

        }
    }

    @Test
    public void getPrivateVariableFromConstructedClassViaVarHandles() {

        try {

            /*
             * TODO:
             *  Replace the "null"s with valid values to get a VarHandle.
             *  Check API: java.lang.invoke.MethodHandles.privateLookupIn(?, ?)
             *  Check API: java.lang.invoke.MethodHandles.Lookup.findVarHandle(?, ?, ?)
             */
            VarHandle privateVariableVarHandle = MethodHandles
                    .privateLookupIn(null, MethodHandles.lookup())
                    .findVarHandle(null, null, null);

            assertEquals("There should only be one coordinateType",
                    1,
                    privateVariableVarHandle.coordinateTypes().size());

            assertEquals("The only coordinate type is AttributeGetterTest",
                    GetterTest.class,
                    privateVariableVarHandle.coordinateTypes().get(0));

            assertTrue("Access mode for a GET should be true",
                    privateVariableVarHandle.isAccessModeSupported(VarHandle.AccessMode.GET));

            assertEquals("The value of the field should be 2",
                    2,
                    privateVariableVarHandle.get(this));

        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {

            fail(TEST_FAILURE.getValue() + e.getMessage());

        }
    }
    /* -------------------------------------------------------------------------------------- */


    /* -------------------------------------------------------------------------------------- */
    /*
     * BEGIN: PRIVATE 1-DIMENSIONAL ARRAY VARIABLE USING REFLECTION AND VARIABLE HANDLES
     */
    @Test
    public void getPrimitiveArrayFromConstructedClassViaReflection() {

        int[] reflectedArray = null;

        try {

            Class<?> clazz = GetterTest.class;

            Field privatePrimitiveArrayVariableField =
                    clazz.getDeclaredField("privatePrimitiveArrayVariable");

            privatePrimitiveArrayVariableField.setAccessible(true);

            Class<?> fieldClass = privatePrimitiveArrayVariableField.getType();

            if (fieldClass.isArray()) {
                reflectedArray = int[].class.cast(privatePrimitiveArrayVariableField.get(this));
            }

            assertEquals("The length of the array should be 3",
                    3,
                    reflectedArray.length);

            assertEquals("The first element of the array should be 1",
                    1,
                    reflectedArray[0]);

            assertEquals("The third of the array should be 3",
                    3,
                    reflectedArray[2]);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                IllegalAccessException e) {

            fail(UNSAFE_FAILURE.getValue() + e.getMessage());

        }

    }

    @Test
    public void getPrivatePrimitiveArrayVariableFromConstructedClassViaVarHandles() {

        try {

            /*
             * TODO:
             *  Replace the "null"s with valid values to get a VarHandle.
             *  Check API: java.lang.invoke.MethodHandles.privateLookupIn(?, ?)
             *  Check API: java.lang.invoke.MethodHandles.Lookup.findVarHandle(?, ?, ?)
             */
            VarHandle privatePrimitiveArrayVariableVarHandle = MethodHandles
                    .privateLookupIn(null, null)
                    .findVarHandle(null, null, null);

            assertEquals("There should only be one coordinateType",
                    1,
                    privatePrimitiveArrayVariableVarHandle.coordinateTypes().size());

            assertEquals("The only coordinate type is AttributeGetterTest",
                    GetterTest.class,
                    privatePrimitiveArrayVariableVarHandle.coordinateTypes().get(0));

            int[] varHandleTypeArray = int[].class.cast(
                    privatePrimitiveArrayVariableVarHandle.get(this));

            /*
             * TODO:
             *  Replace the "null"s with valid values to get a VarHandle.
             *  Check API: java.lang.invoke.MethodHandles.arrayElementVarHandle(?)
             */
            VarHandle arrayElementHandle = MethodHandles.arrayElementVarHandle(null);

            assertEquals("The length of the array should be 3",
                    3,
                    varHandleTypeArray.length);

            assertEquals("The first element of the array should be 1",
                    1,
                    arrayElementHandle.get(privatePrimitiveArrayVariable, 0));

            assertEquals("The third of the array should be 3",
                    3,
                    arrayElementHandle.get(privatePrimitiveArrayVariable, 2));

        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {

            fail(TEST_FAILURE.getValue() + e.getMessage());

        }
    }
    /* -------------------------------------------------------------------------------------- */


    /* -------------------------------------------------------------------------------------- */
    /*
     * BEGIN: PRIVATE 2-DIMENSIONAL ARRAY VARIABLE USING REFLECTION AND VARIABLE HANDLES
     */
    @Test
    public void get2DPrimitiveArrayFromConstructedClassViaReflection() {

        int[][] reflectedArray = null;

        try {

            Class<?> clazz = GetterTest.class;

            Field privatePrimitive2DArrayVariableField =
                    clazz.getDeclaredField("privatePrimitive2DArrayVariable");

            privatePrimitive2DArrayVariableField.setAccessible(true);

            Class<?> fieldClass = privatePrimitive2DArrayVariableField.getType();

            if (fieldClass.isArray()) {
                reflectedArray = int[][].class.cast(privatePrimitive2DArrayVariableField.get(this));
            }

            assertEquals("The length of the array should be 3",
                    3,
                    reflectedArray.length);

            assertEquals("The first of first element of the array should be 1",
                    1,
                    reflectedArray[0][0]);

            assertEquals("The third of third of the array should be 9",
                    9,
                    reflectedArray[2][2]);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                IllegalAccessException e) {

            fail(UNSAFE_FAILURE.getValue() + e.getMessage());

        }

    }

    @Test
    public void getPrivate2DPrimitiveArrayVariableFromConstructedClassViaVarHandles() {

        try {

            /*
             * TODO:
             *  Replace the "null"s with valid values to get a VarHandle.
             *  Check API: java.lang.invoke.MethodHandles.privateLookupIn(?, ?)
             *  Check API: java.lang.invoke.MethodHandles.Lookup.findVarHandle(?, ?, ?)
             */
            VarHandle privatePrimitive2DArrayVariableVarHandle = MethodHandles
                    .privateLookupIn(null, null)
                    .findVarHandle(null, null, null);

            assertEquals("There should only be one coordinateType",
                    1,
                    privatePrimitive2DArrayVariableVarHandle.coordinateTypes().size());

            assertEquals("The only coordinate type is AttributeGetterTest",
                    GetterTest.class,
                    privatePrimitive2DArrayVariableVarHandle.coordinateTypes().get(0));

            int[][] varHandleTypeArray = int[][].class.cast(
                    privatePrimitive2DArrayVariableVarHandle.get(this));

            /*
             * TODO:
             *  Replace the "null"s with valid values to get a VarHandle.
             *  Check API: java.lang.invoke.MethodHandles.arrayElementVarHandle(?)
             */
            VarHandle arrayElementHandle = MethodHandles.arrayElementVarHandle(null);

            assertEquals("The length of the array should be 3",
                    3,
                    varHandleTypeArray.length);

            assertEquals("The first element of the array should be 1",
                    1,
                    ((int[]) arrayElementHandle.get(privatePrimitive2DArrayVariable, 0))[0]);

            assertEquals("The last element of the array should be 9",
                    9,
                    ((int[]) arrayElementHandle.get(privatePrimitive2DArrayVariable, 2))[2]);

        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {

            fail(TEST_FAILURE.getValue() + e.getMessage());

        }
    }

}