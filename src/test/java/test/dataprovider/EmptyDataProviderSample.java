package test.dataprovider;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Iterator;

public class EmptyDataProviderSample {

    @DataProvider
    public Object[][] dp1() {
        return new Object[][]{};
    }

    @Test(dataProvider = "dp1")
    public void test1() {
        Assert.fail();
    }

    @DataProvider(indices = { 2 })
    public Iterator<Object[]> dp2() {
        return Collections.emptyIterator();
    }

    @Test(dataProvider = "dp2")
    public void test2() {
        Assert.fail();
    }
}