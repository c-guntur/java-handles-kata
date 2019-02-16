package none.cvg.methods;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import none.cvg.DemoClass;
import org.junit.Test;

import static none.cvg.ErrorMessages.REFLECTION_FAILURE;
import static none.cvg.ErrorMessages.TEST_FAILURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/*
 * TODO:
 *  This test aims at using MethodHandles to invoke a public static method on a class.
 *  Each solved test shows how this can be achieved with the traditional reflection calls.
 *  Each unsolved test provides a few hints that will allow the kata-taker to manually solve
 *  the exercise to achieve the same goal with MethodHandles.
 */
public class PublicStaticMethodInvocationTest {

    @Test
    public void reflectionPublicStaticMethod() {

        String expectedOutput = "DemoClass.class - Public static method via reflection";

        try {

            // Find the method on the class via a getMethod.
            Method publicStaticMethod =
                    DemoClass.class.getMethod("publicStaticMethod",
                            String.class);

            assertEquals("Reflection invocation failed",
                    expectedOutput,
                    publicStaticMethod.invoke(DemoClass.class, "via reflection"));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

            fail(REFLECTION_FAILURE.getValue() + e.getMessage());
        }
    }

    @Test
    public void methodHandlePublicStaticMethod() {

        String expectedOutput = "DemoClass.class - Public static method via Method Handles";

        /*
         * TODO:
         *  Get a public lookup from java.lang.invoke.MethodHandles
         *  Public parts of a class are looked up via "public lookups"
         *  Check API: java.lang.invoke.MethodHandles.publicLookup()
         */
        MethodHandles.Lookup publicStaticMethodHandlesLookup = null;

        /*
         * TODO:
         *  Replace the "null"s with valid values to get a signature for a publicStaticMethod.
         *  Create a methodType instance that matches the signature of what we wish to invoke
         *  The publicMethod() returns a String and accepts a String parameter
         *  Check API: java.lang.invoke.MethodType.methodType(?, ?)
         */
        MethodType methodType = null; // HINT: MethodType.methodType(null, null);

        try {

            /*
             * TODO:
             *  Replace the "null"s with valid values to get a handle to publicStaticMethod.
             *  "Find" a method of the class via the Lookup instance,
             *  based on the methodType described above and the method name.
             *  Public static methods can be searched via the findStatic() method.
             *  Check API: java.lang.invoke.MethodHandles.Lookup.findStatic(?, ?, ?)
             */
            MethodHandle publicStaticMethodHandle =
                    publicStaticMethodHandlesLookup.findStatic(null,
                            null, null); // Class, Method name, Method type

            /*
             * ATTENTION: The invoke() method does not take an instance of demoClass as its
             *             first parameter for static method invocation.
             */
            assertEquals("Method handles invocation failed",
                    expectedOutput,
                    publicStaticMethodHandle.invoke(
                            "via Method Handles"));

        } catch (NoSuchMethodException | IllegalAccessException e) {

            fail("Failed to execute a public static method invocation via Method Handles: "
                    + e.getMessage());
        } catch (Throwable t) {

            // invoke throws a Throwable (hence catching Throwable separately).
            fail(TEST_FAILURE.getValue() + t.getMessage());
        }
    }
}