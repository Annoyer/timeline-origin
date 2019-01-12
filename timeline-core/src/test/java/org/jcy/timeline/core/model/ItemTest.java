package org.jcy.timeline.core.model;

import org.jcy.timeline.test.util.EqualsTester;
import com.squareup.burst.BurstJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jcy.timeline.test.util.EqualsTester.newInstance;
import static java.lang.Math.signum;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(BurstJUnit4.class)
public class ItemTest {

    @Test
    public void initialization() {
        FakeItem actual = new FakeItem("1", 10L);

        ItemAssert.assertThat(actual)
                .hasId("1")
                .hasTimeStamp(10L);
    }

    @Test
    public void compareToWithDifferentItems(CompareItemData data) {
        ItemAssert.assertThat(data.getX()).isPrecedentTo(data.getY());
        ItemAssert.assertThat(data.getY()).isSubsequentTo(data.getX());
        assertThat(signum(data.getX().compareTo(data.getY())) == -signum(data.getY().compareTo(data.getX())));
    }

    @Test
    public void compareToWithCoincidentItems() {
        FakeItem first = new FakeItem("1", 10L);
        FakeItem second = new FakeItem("1", 10L);
        FakeItem subSequent = new FakeItem("1", 20L);

        ItemAssert.assertThat(first).isCoincidentTo(second);
        ItemAssert.assertThat(second).isCoincidentTo(first);
        assertThat(signum(first.compareTo(second)) == -signum(second.compareTo(first)));
        assertThat(signum(first.compareTo(subSequent)) == signum(second.compareTo(subSequent)));
    }

    @Test
    public void equalsAndHashCode() {
        EqualsTester<FakeItem> equalsTester = newInstance(new FakeItem("1", 10L));
        equalsTester.assertEqual(new FakeItem("1", 10L), new FakeItem("1", 10L));
        equalsTester.assertEqual(new FakeItem("1", 10L), new FakeItem("1", 10L), new FakeItem("1", 10L));
        equalsTester.assertNotEqual(new FakeItem("1", 20L), new FakeItem("2", 30L));
        equalsTester.assertNotEqual(new FakeItem("1", 20L), new FakeItem("1", 30L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNegativeTimeStamp() {
        new FakeItem("1", -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNullAsId() {
        new FakeItem(null, 0L);
    }
}