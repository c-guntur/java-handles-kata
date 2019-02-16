package none.cvg.constructors;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import none.cvg.DemoClass;
import org.junit.Test;

import static none.cvg.ErrorMessages.REFLECTION_FAILURE;
import static none.cvg.ErrorMessages.TEST_FAILURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/*
 * TODO:
 *  This test aims at using MethodHandles to invoke a constructor with a parameter on a class in
 *  order to create a new instance.
 *  Each solved test shows how this can be achieved with the traditional reflection calls.
 *  Each unsolved test provides a few hints that will allow the kata-taker to manually solve
 *  the exercise to achieve the same goal with MethodHandles.
 */
public class ParameteredConstructorInvocationTest {

    @Test
    public void reflectionParamConstructor() {

        String expectedOutput = "[Constructor Demo]" +
                " - Constructor via reflection";

        try {

            Class<DemoClass> demoClassClass =
                    (Class<DemoClass>) Class.forName("none.cvg.DemoClass");

            Constructor<DemoClass> demoClassConstructor =
                    demoClassClass.getConstructor(String.class);

            DemoClass demoClass =
                    demoClassConstructor.newInstance("Constructor Demo");

            assertEquals(
                    "Reflection invocation failed",
                    expectedOutput,
                    demoClass.printStuff("Constructor via reflection"));

        } catch (ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException | InvocationTargetException e) {

            fail(REFLECTION_FAILURE.getValue() + e.getMessage());
        }
    }


    @Test
    public void methodHandleParamConstructor() {

        String expectedOutput = "[Constructor Demo] - Constructor via Method Handles";

        /*
         * TODO:
         *  The API provides a few lookup mechanisms. For a public constructor, on a
         *  public class, we can use the lookup with minimal checks and trust.
         *  Public members of public classes are looked up via "public lookups"
         *  Check API: java.lang.invoke.MethodHandles.publicLookup()
         */
        MethodHandles.Lookup publicMethodHandlesLookup = null;

        /*
         * TODO:
         *  Create a methodType instance that matches the constructor that takes a string param
         *  Constructors should have a void return type
         *  This constructor has a string parameter
         *  Search for method that: has a return type of void (Constructor)
         *  and accepts a String parameter.
         *  Check API: java.lang.invoke.MethodType.methodType(?, ?)
         */
        MethodType methodType = null; // HINT: MethodType.methodType(?, ?);

        try {

            /*
             * TODO:
             *  Replace the "nulls" to find a constructor handle for DemoClass using methodType
             *  "Find" a constructor of the class via the Lookup instance,
             *  based on the methodType described above
             *  Check API: java.lang.invoke.MethodHandles.Lookup.findConstructor(?, ?)
             */
            MethodHandle demoClassConstructor =
                    publicMethodHandlesLookup.findConstructor(null, null);
            // Hint: Class and MethodType

            /*
             * TODO:
             *  Invoke the constructor and pass in "Constructor Demo" as the parameter.
             *  Create an instance of the DemoClass by invoking the method handle
             *  The MethodHandle has two methods invoke() and invokeExact()
             *  The invoke() is good for conversion/substitution of param types
             *  The invokeExact() is great if there is no ambiguity
             *  Check API: java.lang.invoke.MethodHandle.invokeExact(?)
             */
            DemoClass demoClass =
                    null; //HINT: invokeExact(
            //constructor argument(s)); Requires casting.

            assertEquals("Method handles invocation failed",
                    expectedOutput,
                    demoClass.printStuff(
                            "Constructor via Method Handles"));

        } catch (NoSuchMethodException | IllegalAccessException e) {

            fail("Failed to execute a constructor invocation via Method Handles: "
                    + e.getMessage());
        } catch (Throwable t) {

            // invokeExact() throws a Throwable (hence catching Throwable separately).
            fail(TEST_FAILURE.getValue() + t.getMessage());
        }
    }
}