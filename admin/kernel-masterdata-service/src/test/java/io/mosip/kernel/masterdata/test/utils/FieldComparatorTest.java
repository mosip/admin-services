package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.utils.FieldComparator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDate;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FieldComparatorTest {

    private static final String NAME_FIELD = "name";
    private static final String AGE_FIELD = "age";
    private static final String DOB_FIELD = "dob";

    @Test
    public void testCompare_String_Ascending() throws Exception {
        FieldComparator<TestObject> comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort(NAME_FIELD, "ASC"));
        TestObject obj1 = new TestObject("Alice");
        TestObject obj2 = new TestObject("Bob");

        int result = comparator.compare(obj1, obj2);
        Assert.assertEquals(-1, result);

        comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort(NAME_FIELD, "DESC"));
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompare_Integer_Ascending() throws Exception {
        FieldComparator<TestObject> comparator = new FieldComparator<>(getDeclaredField(TestObject.class, AGE_FIELD), new SearchSort(AGE_FIELD, "ASC"));
        TestObject obj1 = new TestObject(25);
        TestObject obj2 = new TestObject(30);

        int result = comparator.compare(obj1, obj2);
        Assert.assertEquals(-1, result);

        comparator = new FieldComparator<>(getDeclaredField(TestObject.class, AGE_FIELD), new SearchSort(AGE_FIELD, "DESC"));
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompare_Boolean_Ascending() throws Exception {
        FieldComparator<TestObject> comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort("isActive", "ASC"));
        TestObject obj1 = new TestObject(String.valueOf(true));
        TestObject obj2 = new TestObject(String.valueOf(false));

        int result = comparator.compare(obj1, obj2);
        Assert.assertEquals(14, result);

        comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort("isActive", "DESC"));
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(-14, result);
    }

    @Test
    public void testCompare_Double_Ascending() throws Exception {
        FieldComparator<TestObject> comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort("salary", "ASC"));
        TestObject obj1 = new TestObject(String.valueOf(2500.50));
        TestObject obj2 = new TestObject(String.valueOf(3000.75));

        int result = comparator.compare(obj1, obj2);
        Assert.assertEquals(-1, result);

        comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort("salary", "DESC"));
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompare_Long_Ascending() throws Exception {
        FieldComparator<TestObject> comparator = new FieldComparator<>(getDeclaredField(TestObject.class, AGE_FIELD), new SearchSort("id", "ASC"));
        TestObject obj1 = new TestObject(String.valueOf(123456L));
        TestObject obj2 = new TestObject(String.valueOf(987654L));

        int result = comparator.compare(obj1, obj2);
        Assert.assertEquals(0, result);

        comparator = new FieldComparator<>(getDeclaredField(TestObject.class, AGE_FIELD), new SearchSort("id", "DESC"));
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompare_Float_Ascending() throws Exception {
        FieldComparator<TestObject> comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort("percentage", "ASC"));
        TestObject obj1 = new TestObject(String.valueOf(3.14f));
        TestObject obj2 = new TestObject(String.valueOf(2.72f));

        int result = comparator.compare(obj1, obj2);
        Assert.assertEquals(1, result);

        comparator = new FieldComparator<>(getDeclaredField(TestObject.class, NAME_FIELD), new SearchSort("percentage", "DESC"));
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompare_LocalDate_Ascending() throws Exception {
        FieldComparator<TestObject> comparator = new FieldComparator<>(getDeclaredField(TestObject.class, DOB_FIELD), new SearchSort(DOB_FIELD, "ASC"));
        TestObject obj1 = new TestObject(LocalDate.of(2000, 1, 1));
        TestObject obj2 = new TestObject(LocalDate.of(2001, 1, 1));

        int result = comparator.compare(obj1, obj2);
        Assert.assertEquals(-1, result);

        comparator = new FieldComparator<>(getDeclaredField(TestObject.class, DOB_FIELD), new SearchSort(DOB_FIELD, "DESC"));
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(1, result);

        obj1.setDob(null);
        result = comparator.compare(obj1, obj2);
        Assert.assertEquals(1, result);
    }

    private Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }

    class TestObject {
        private String name;
        private int age;
        private LocalDate dob;

        public TestObject(String name) {
            this.name = name;
        }

        public TestObject(int age) {
            this.age = age;
        }

        public TestObject(LocalDate dob) {
            this.dob = dob;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public LocalDate getDob() {
            return dob;
        }

        public void setDob(LocalDate dob) {
            this.dob = dob;
        }
    }

}
