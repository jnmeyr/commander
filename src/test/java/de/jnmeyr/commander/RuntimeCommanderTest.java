package de.jnmeyr.commander;

import de.jnmeyr.commander.arguments.BooleanArgument;
import de.jnmeyr.commander.arguments.IntegerArgument;
import org.junit.Assert;
import org.junit.Test;

public final class RuntimeCommanderTest {

    private final class ExampleRuntimeCommander extends RuntimeCommander {

        @BooleanArgument
        protected boolean b1;

        @BooleanArgument(fallback = false)
        protected boolean b2;

        @BooleanArgument(fallback = true)
        protected boolean b3;

        @IntegerArgument
        protected int i1;

        @IntegerArgument(fallback = 0)
        protected int i2;

        @IntegerArgument(fallback = 5)
        protected int i3;

        public ExampleRuntimeCommander(final String[] strings) {
            super(strings);
        }

    }

    @Test
    public void testNull() {
        final ExampleRuntimeCommander exampleRuntimeCommander = new ExampleRuntimeCommander(null);

        Assert.assertFalse(exampleRuntimeCommander.b1);
        Assert.assertFalse(exampleRuntimeCommander.b2);
        Assert.assertTrue(exampleRuntimeCommander.b3);
        Assert.assertEquals(exampleRuntimeCommander.i1, 0);
        Assert.assertEquals(exampleRuntimeCommander.i2, 0);
        Assert.assertEquals(exampleRuntimeCommander.i3, 5);
    }

    @Test
    public void testEmpty() {
        final String[] strings = new String[0];

        final ExampleRuntimeCommander exampleRuntimeCommander = new ExampleRuntimeCommander(strings);

        Assert.assertFalse(exampleRuntimeCommander.b1);
        Assert.assertFalse(exampleRuntimeCommander.b2);
        Assert.assertTrue(exampleRuntimeCommander.b3);
        Assert.assertEquals(exampleRuntimeCommander.i1, 0);
        Assert.assertEquals(exampleRuntimeCommander.i2, 0);
        Assert.assertEquals(exampleRuntimeCommander.i3, 5);
    }

    @Test
    public void testGarbage() {
        final String[] strings = new String[] {
                "this", "is", "garbage"
        };

        final ExampleRuntimeCommander exampleRuntimeCommander = new ExampleRuntimeCommander(strings);

        Assert.assertFalse(exampleRuntimeCommander.b1);
        Assert.assertFalse(exampleRuntimeCommander.b2);
        Assert.assertTrue(exampleRuntimeCommander.b3);
        Assert.assertEquals(exampleRuntimeCommander.i1, 0);
        Assert.assertEquals(exampleRuntimeCommander.i2, 0);
        Assert.assertEquals(exampleRuntimeCommander.i3, 5);
    }

    @Test
    public void testFallbacks() {
        final String[] strings = new String[] {
                "b1=false", "b2=false", "b3=false",
                "i1=0", "i2=0", "i3=0"
        };

        final ExampleRuntimeCommander exampleRuntimeCommander = new ExampleRuntimeCommander(strings);

        Assert.assertFalse(exampleRuntimeCommander.b1);
        Assert.assertFalse(exampleRuntimeCommander.b2);
        Assert.assertFalse(exampleRuntimeCommander.b3);
        Assert.assertEquals(exampleRuntimeCommander.i1, 0);
        Assert.assertEquals(exampleRuntimeCommander.i2, 0);
        Assert.assertEquals(exampleRuntimeCommander.i3, 0);
    }

    @Test
    public void testValues() {
        final String[] strings = new String[] {
                "b1", "b2=false", "b3=true",
                "i1=-1000", "i2=0", "i3=666"
        };

        final ExampleRuntimeCommander exampleRuntimeCommander = new ExampleRuntimeCommander(strings);

        Assert.assertTrue(exampleRuntimeCommander.b1);
        Assert.assertFalse(exampleRuntimeCommander.b2);
        Assert.assertTrue(exampleRuntimeCommander.b3);
        Assert.assertEquals(exampleRuntimeCommander.i1, -1000);
        Assert.assertEquals(exampleRuntimeCommander.i2, 0);
        Assert.assertEquals(exampleRuntimeCommander.i3, 666);
    }

}
