import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TwoThreeTreeGivenTests {

    @Test
    public void singleNodeTree() {
        TwoThreeTree t = new TwoThreeTree();
        int val = 9;
        t.insert(val);
        String expected = "9";

        assertEquals(expected, t.search(val));
        val = 8;
        assertEquals(expected, t.search(val));
        val = 10;
        assertEquals(expected, t.search(val));

        val = 15;
        t.insert(val);
        expected = "9 15";
        val = 9;
        assertEquals(expected, t.search(val));
        val = 8;
        assertEquals(expected, t.search(val));
        val = 10;
        assertEquals(expected, t.search(val));
        val = 15;
        assertEquals(expected, t.search(val));
        val = 18;
        assertEquals(expected, t.search(val));

        t = new TwoThreeTree();
        val = 15;
        t.insert(val);
        val = 9;
        t.insert(val);
        val = 9;
        assertEquals(expected, t.search(val));
        val = 8;
        assertEquals(expected, t.search(val));
        val = 10;
        assertEquals(expected, t.search(val));
        val = 15;
        assertEquals(expected, t.search(val));
        val = 18;
        assertEquals(expected, t.search(val));

    }

    @Test
    public void oneSplitLeft() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(9);
        t.insert(15);
        t.insert(1);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "15";
        assertEquals(expected, t.search(15));
        assertEquals(expected, t.search(17));
        assertEquals(expected, t.search(11));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));
    }

    @Test
    public void oneSplitRight() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(1);
        t.insert(9);
        t.insert(15);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "15";
        assertEquals(expected, t.search(15));
        assertEquals(expected, t.search(17));
        assertEquals(expected, t.search(11));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));
    }

    @Test
    public void oneSplitMiddle() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(1);
        t.insert(15);
        t.insert(9);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "15";
        assertEquals(expected, t.search(15));
        assertEquals(expected, t.search(17));
        assertEquals(expected, t.search(11));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));
    }


    @Test
    public void customTestOne(){
        TwoThreeTree t = new TwoThreeTree();
        t.insert(42);
        t.insert(81);
        t.insert(80);
        t.insert(16);
        t.insert(54);
        t.insert(71);
        t.insert(34);
        t.insert(67);
        t.insert(18);
        t.insert(75);
        t.insert(40);
        t.insert(68);
        t.insert(77);
        t.insert(42);
        t.insert(97);
        t.insert(40);
        t.insert(69);
        t.insert(30);
        t.insert(78);
        t.insert(9);
        t.insert(36);
        t.insert(60);
        t.insert(66);
        t.insert(9);
        t.insert(63);
        t.insert(75);
        t.insert(52);
        t.insert(20);
        t.insert(10);
        t.insert(86);
        t.insert(72);
        t.insert(89);
        t.insert(56);
        t.insert(70);
        t.insert(47);
        t.insert(76);
        t.insert(56);
        t.insert(3);
        t.insert(87);
        t.insert(73);
        t.insert(5);
        t.insert(61);
        t.insert(98);
        t.insert(90);
        t.insert(78);
        t.insert(74);
        t.insert(95);
        t.insert(45);
        t.insert(85);
        t.insert(89);
        t.insert(13);
        t.insert(55);
        t.insert(83);
        t.insert(6);
        t.insert(49);
        t.insert(3);
        t.insert(30);
        t.insert(12);
        t.insert(3);
        t.insert(32);
        t.insert(39);
        t.insert(94);
        t.insert(39);
        t.insert(61);
        t.insert(81);
        t.insert(53);
        t.insert(9);
        t.insert(42);
        t.insert(50);
        t.insert(33);
        t.insert(54);
        t.insert(94);
        t.insert(84);
        t.insert(79);
        t.insert(47);
        t.insert(53);
        t.insert(37);
        t.insert(33);
        t.insert(27);
        t.insert(62);
        t.insert(71);
        t.insert(40);
        t.insert(89);
        t.insert(35);
        t.insert(98);
        t.insert(40);
        t.insert(73);
        t.insert(21);
        t.insert(34);
        t.insert(83);
        t.insert(47);
        t.insert(64);
        t.insert(89);
        t.insert(31);
        t.insert(71);
        t.insert(14);
        t.insert(88);
        t.insert(43);
        t.insert(34);
        t.insert(15);

        assertEquals("3", t.search(3));
        assertEquals("5", t.search(5));
        assertEquals("6 9", t.search(6));
        assertEquals("6 9", t.search(9));
        assertEquals("10", t.search(10));
        assertEquals("12", t.search(12));

        //Errors here
        assertEquals("13 15", t.search(13));


        assertEquals("14", t.search(14));
        assertEquals("13 15", t.search(15));
        assertEquals("16", t.search(16));
        assertEquals("18 34", t.search(18));
        assertEquals("20", t.search(20));
        assertEquals("21", t.search(21));
        assertEquals("27", t.search(27));
        assertEquals("30", t.search(30));
        assertEquals("31", t.search(31));
        assertEquals("32", t.search(32));
        assertEquals("33", t.search(33));
        assertEquals("18 34", t.search(34));
        assertEquals("35", t.search(35));
        assertEquals("36 39", t.search(36));
        assertEquals("37", t.search(37));
        assertEquals("36 39", t.search(39));
        assertEquals("40", t.search(40));
        assertEquals("42 54", t.search(42));
        assertEquals("43 45", t.search(43));
        assertEquals("43 45", t.search(45));
        assertEquals("47 52", t.search(47));
        assertEquals("49 50", t.search(49));
        assertEquals("49 50", t.search(50));
        assertEquals("47 52", t.search(52));
        assertEquals("53", t.search(53));
        assertEquals("42 54", t.search(54));
        assertEquals("55 56", t.search(55));
        assertEquals("55 56", t.search(56));
        assertEquals("60 63", t.search(60));
        assertEquals("61 62", t.search(61));
        assertEquals("61 62", t.search(62));
        assertEquals("60 63", t.search(63));
        assertEquals("64 66", t.search(64));
        assertEquals("64 66", t.search(66));
        assertEquals("67", t.search(67));
        assertEquals("68", t.search(68));
        assertEquals("69", t.search(69));
        assertEquals("70", t.search(70));
        assertEquals("71", t.search(71));
        assertEquals("72", t.search(72));
        assertEquals("73 75", t.search(73));
        assertEquals("74", t.search(74));
        assertEquals("73 75", t.search(75));
        assertEquals("76", t.search(76));
        assertEquals("77", t.search(77));
        assertEquals("78 79", t.search(78));
        assertEquals("78 79", t.search(79));
        assertEquals("80 83", t.search(80));
        assertEquals("81", t.search(81));
        assertEquals("80 83", t.search(83));
        assertEquals("84 85", t.search(84));
        assertEquals("84 85", t.search(85));
        assertEquals("86 94", t.search(86));
        assertEquals("87 88", t.search(87));
        assertEquals("87 88", t.search(88));
        assertEquals("89", t.search(89));
        assertEquals("90", t.search(90));
        assertEquals("86 94", t.search(94));
        assertEquals("95", t.search(95));
        assertEquals("97", t.search(97));
        assertEquals("98", t.search(98));

//        for(int i = 0 ; i < 100 ; i++){
//            System.out.println("t.insert(" + ThreadLocalRandom.current().nextInt(0, 100) + ");");
//        }


    }

    @Test
    public void testDuplicates() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(1);
        t.insert(9);
        t.insert(15);
        t.insert(13);
        t.insert(20);
        t.insert(7);
        t.insert(4);
        t.insert(1);
        t.insert(9);
        t.insert(15);
        t.insert(1);
        t.insert(9);
        t.insert(15);
        t.insert(13);
        t.insert(20);
        t.insert(7);
        t.insert(4);
        t.insert(13);
        t.insert(20);
        t.insert(7);
        t.insert(4);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "4";
        assertEquals(expected, t.search(4));
        expected = "15";
        assertEquals(expected, t.search(15));

        expected = "13";
        assertEquals(expected, t.search(12));
        assertEquals(expected, t.search(13));
        assertEquals(expected, t.search(14));
        expected = "20";
        assertEquals(expected, t.search(19));
        assertEquals(expected, t.search(20));
        assertEquals(expected, t.search(21));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));

        expected = "7";
        assertEquals(expected, t.search(6));
        assertEquals(expected, t.search(7));
        assertEquals(expected, t.search(8));

    }


}
