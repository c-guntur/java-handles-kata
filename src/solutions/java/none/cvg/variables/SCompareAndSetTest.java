package none.cvg.variables;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import org.junit.Test;
import sun.misc.Unsafe;

import static none.cvg.ErrorMessages.TEST_FAILURE;
import static none.cvg.ErrorMessages.UNSAFE_FAILURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*
 * DONE:
 *  This test aims at using VarHandles to compare and then set a value of a given variable.
 *  Each solved test shows how this can be achieved with the traditional reflection calls.
 *  Each unsolved test provides a few hints that will allow the kata-taker to manually solve
 *  the exercise to achieve the same goal with MethodHandles.
 */
public class SCompareAndSetTest {

    private Integer currentValue = 2;

    private volatile Integer privateVolatile = 2;

    private Integer newValue = 7;

    @Test
    public void compareAndSetUsingAtomicReference() {

        AtomicReference<Integer> atomicReference = new AtomicReference<>(privateVolatile);


        boolean exchanged = atomicReference.compareAndSet(privateVolatile, newValue);

        assertTrue("The value should have been changed to 7, " +
                        "hence exchanged should be true",
                exchanged);

        assertEquals("The value of the privateVolatile should now be 7",
                newValue,
                atomicReference.get());

        exchanged = atomicReference.compareAndSet(privateVolatile, newValue);

        assertFalse("The value should not have changed again, " +
                        "hence exchanged should be false",
                exchanged);

        assertEquals("The value of the privateVolatile should still be 7",
                newValue,
                atomicReference.get());

    }

    @Test
    public void compareAndSetUsingAtomicReferenceFieldUpdater() {

        final AtomicReferenceFieldUpdater<SCompareAndSetTest, Integer> valueUpdater =
                AtomicReferenceFieldUpdater.newUpdater(SCompareAndSetTest.class,
                        Integer.class,
                        "privateVolatile");

        boolean exchanged = valueUpdater.compareAndSet(this, currentValue, newValue);

        assertTrue("The value should have been changed to 7, " +
                        "hence exchanged should be true",
                exchanged);

        assertEquals("The value of the privateVolatile should now be 7",
                newValue,
                valueUpdater.get(this));

        exchanged = valueUpdater.compareAndSet(this, 2, 33);

        assertFalse("The value should not have changed since the expected value " +
                        "did not match, hence exchanged should be false",
                exchanged);

        assertEquals("The value of the privateVolatile should still be 7",
                newValue,
                valueUpdater.get(this));
    }

    @Test
    public void compareAndSetUsingUnsafe() {

        try {

            Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeInstance.setAccessible(true);
            final Unsafe unsafe = (Unsafe) theUnsafeInstance.get(null);

            final long offset;

            offset = unsafe.objectFieldOffset(
                    SCompareAndSetTest.class.getDeclaredField("privateVolatile"));

            boolean exchanged = unsafe.compareAndSwapObject(this,
                    offset, currentValue, newValue);

            assertTrue("The value should have been changed to 7, " +
                            "hence exchanged should be true",
                    exchanged);

            assertEquals("The value of the privateVolatile should now be 7",
                    newValue,
                    unsafe.getObject(this, offset));

            exchanged = unsafe.compareAndSwapObject(this, offset, 2, 33);

            assertFalse("The value should not have changed since the expected value " +
                            "did not match, hence exchanged should be false",
                    exchanged);

            assertEquals("The value of the privateVolatile should still be 7",
                    newValue,
                    unsafe.getObject(this, offset));

        } catch (NoSuchFieldException | IllegalAccessException e) {

            fail(UNSAFE_FAILURE.getValue() + e.getMessage());

        }

    }

    @Test
    public void compareAndSetUsingVarHandles() {

        VarHandle varHandle = null;

        try {

            /*
             * DONE:
             *  Replace the "null"s with valid values to get a VarHandle.
             *  Check API: java.lang.invoke.MethodHandles.privateLookupIn(?, ?)
             *             HINT: params are Target class, Lookup type
             *  Check API: java.lang.invoke.MethodHandles.Lookup.findVarHandle(?, ?, ?)
             *             HINT: params are Declaring class, Variable name, Variable type
             */
            varHandle = MethodHandles
                    .privateLookupIn(SCompareAndSetTest.class, MethodHandles.lookup())
                    .findVarHandle(SCompareAndSetTest.class, "privateVolatile", Integer.class);

            /*
             * DONE:
             *  Replace the "false" to a compareAndSet call from 'currentValue' to 'newValue'.
             *  Check API: java.lang.invoke.VarHandle.compareAndSet(...)
             *  Three parameters are needed here:
             *      1. Instance of the class where the variable is being manipulated.
             *      2. The current value to compare
             *      3. The new value to set
             */
            boolean exchanged = varHandle.compareAndSet(this, currentValue, newValue);

            assertTrue("The value should have been changed to 7, " +
                            "hence exchanged should be true",
                    exchanged);

            assertEquals("The value of the privateVolatile should now be 7",
                    newValue,
                    varHandle.get(this));

            /*
             * TODO:
             *  Replace the "false" to a compareAndSet call from 2 to 33.
             *  Check API: java.lang.invoke.VarHandle.compareAndSet(...)
             *  Three parameters are needed here:
             *      1. Instance of the class where the variable is being manipulated.
             *      2. The current value to compare
             *      3. The new value to set
             */
            exchanged = varHandle.compareAndSet(this, 2, 33);

            assertFalse("The value should not have changed since the expected value " +
                            "did not match, hence exchanged should be false",
                    exchanged);

            assertEquals("The value of the privateVolatile should still be 7",
                    newValue,
                    varHandle.get(this));

        } catch (NoSuchFieldException | IllegalAccessException e) {

            fail(TEST_FAILURE.getValue() + e.getMessage());

        }

    }
}
