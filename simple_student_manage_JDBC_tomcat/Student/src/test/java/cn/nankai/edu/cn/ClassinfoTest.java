package cn.nankai.edu.cn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Classinfo实体类测试")
class ClassinfoTest {

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        Classinfo classinfo = new Classinfo();
        
        assertNotNull(classinfo);
        assertNull(classinfo.getName());
        assertEquals(0, classinfo.getNum());
        assertEquals(0, classinfo.getTeaid());
        assertNull(classinfo.getTeaname());
    }

    @ParameterizedTest
    @CsvSource({
            "计算机一班, 30, 1",
            "计算机二班, 28, 2",
            "软件工程班, 25, 3"
    })
    @DisplayName("测试三参数构造函数")
    void testThreeParameterConstructor(String name, int num, int teaid) {
        Classinfo classinfo = new Classinfo(name, num, teaid);
        
        assertEquals(name, classinfo.getName());
        assertEquals(num, classinfo.getNum());
        assertEquals(teaid, classinfo.getTeaid());
        assertNull(classinfo.getTeaname());
    }

    @ParameterizedTest
    @CsvSource({
            "计算机一班, 30, 1, 张老师",
            "计算机二班, 28, 2, 李老师",
            "软件工程班, 25, 3, 王老师"
    })
    @DisplayName("测试四参数构造函数")
    void testFourParameterConstructor(String name, int num, int teaid, String teaname) {
        Classinfo classinfo = new Classinfo(name, num, teaid, teaname);
        
        assertEquals(name, classinfo.getName());
        assertEquals(num, classinfo.getNum());
        assertEquals(teaid, classinfo.getTeaid());
        assertEquals(teaname, classinfo.getTeaname());
    }

    @Test
    @DisplayName("测试Setter方法")
    void testSetters() {
        Classinfo classinfo = new Classinfo();
        
        classinfo.setName("测试班级");
        classinfo.setNum(50);
        classinfo.setTeaid(10);
        
        assertEquals("测试班级", classinfo.getName());
        assertEquals(50, classinfo.getNum());
        assertEquals(10, classinfo.getTeaid());
    }

    @Test
    @DisplayName("测试边界值 - 人数为0")
    void testBoundaryZeroNum() {
        Classinfo classinfo = new Classinfo("班级", 0, 1);
        assertEquals(0, classinfo.getNum());
        
        classinfo.setNum(0);
        assertEquals(0, classinfo.getNum());
    }

    @Test
    @DisplayName("测试边界值 - 人数为负数")
    void testBoundaryNegativeNum() {
        Classinfo classinfo = new Classinfo("班级", -1, 1);
        assertEquals(-1, classinfo.getNum());
        
        classinfo.setNum(-100);
        assertEquals(-100, classinfo.getNum());
    }

    @Test
    @DisplayName("测试边界值 - 人数为最大值")
    void testBoundaryMaxNum() {
        Classinfo classinfo = new Classinfo("班级", Integer.MAX_VALUE, 1);
        assertEquals(Integer.MAX_VALUE, classinfo.getNum());
        
        classinfo.setNum(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, classinfo.getNum());
    }

    @Test
    @DisplayName("测试边界值 - 教师ID为0")
    void testBoundaryZeroTeaid() {
        Classinfo classinfo = new Classinfo("班级", 30, 0);
        assertEquals(0, classinfo.getTeaid());
        
        classinfo.setTeaid(0);
        assertEquals(0, classinfo.getTeaid());
    }

    @Test
    @DisplayName("测试边界值 - 教师ID为负数")
    void testBoundaryNegativeTeaid() {
        Classinfo classinfo = new Classinfo("班级", 30, -1);
        assertEquals(-1, classinfo.getTeaid());
        
        classinfo.setTeaid(-5);
        assertEquals(-5, classinfo.getTeaid());
    }

    @Test
    @DisplayName("测试边界值 - 教师ID为最大值")
    void testBoundaryMaxTeaid() {
        Classinfo classinfo = new Classinfo("班级", 30, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, classinfo.getTeaid());
        
        classinfo.setTeaid(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, classinfo.getTeaid());
    }

    @Test
    @DisplayName("测试空班级名")
    void testNullName() {
        Classinfo classinfo = new Classinfo(null, 30, 1);
        assertNull(classinfo.getName());
        
        classinfo.setName(null);
        assertNull(classinfo.getName());
    }

    @Test
    @DisplayName("测试空字符串班级名")
    void testEmptyName() {
        Classinfo classinfo = new Classinfo("", 30, 1);
        assertEquals("", classinfo.getName());
        
        classinfo.setName("");
        assertEquals("", classinfo.getName());
    }

    @Test
    @DisplayName("测试空教师名")
    void testNullTeaname() {
        Classinfo classinfo = new Classinfo("班级", 30, 1, null);
        assertNull(classinfo.getTeaname());
    }

    @Test
    @DisplayName("测试空字符串教师名")
    void testEmptyTeaname() {
        Classinfo classinfo = new Classinfo("班级", 30, 1, "");
        assertEquals("", classinfo.getTeaname());
    }

    @Test
    @DisplayName("测试长班级名")
    void testLongName() {
        String longName = "这是一个非常长的班级名称".repeat(10);
        Classinfo classinfo = new Classinfo(longName, 30, 1);
        assertEquals(longName, classinfo.getName());
        
        classinfo.setName(longName);
        assertEquals(longName, classinfo.getName());
    }

    @Test
    @DisplayName("测试长教师名")
    void testLongTeaname() {
        String longTeaname = "这是一个非常长的教师名称".repeat(10);
        Classinfo classinfo = new Classinfo("班级", 30, 1, longTeaname);
        assertEquals(longTeaname, classinfo.getTeaname());
    }

    @Test
    @DisplayName("测试特殊字符班级名")
    void testSpecialCharactersName() {
        String specialName = "班级@#$%^&*()_+{}[]|\\:;\"'<>,.?/~`";
        Classinfo classinfo = new Classinfo(specialName, 30, 1);
        assertEquals(specialName, classinfo.getName());
        
        classinfo.setName(specialName);
        assertEquals(specialName, classinfo.getName());
    }

    @Test
    @DisplayName("测试Unicode字符班级名")
    void testUnicodeName() {
        String unicodeName = "班级中日文㍿㍾㍽";
        Classinfo classinfo = new Classinfo(unicodeName, 30, 1);
        assertEquals(unicodeName, classinfo.getName());
        
        classinfo.setName(unicodeName);
        assertEquals(unicodeName, classinfo.getName());
    }

    @Test
    @DisplayName("测试组合边界值")
    void testCombinedBoundaryValues() {
        Classinfo classinfo = new Classinfo();
        
        classinfo.setName("");
        classinfo.setNum(Integer.MIN_VALUE);
        classinfo.setTeaid(Integer.MIN_VALUE);
        
        assertEquals("", classinfo.getName());
        assertEquals(Integer.MIN_VALUE, classinfo.getNum());
        assertEquals(Integer.MIN_VALUE, classinfo.getTeaid());
    }

    @Test
    @DisplayName("测试多次设置相同值")
    void testMultipleSetSameValue() {
        Classinfo classinfo = new Classinfo();
        
        classinfo.setName("测试班级");
        classinfo.setName("测试班级");
        classinfo.setName("测试班级");
        assertEquals("测试班级", classinfo.getName());
        
        classinfo.setNum(50);
        classinfo.setNum(50);
        classinfo.setNum(50);
        assertEquals(50, classinfo.getNum());
    }

    @Test
    @DisplayName("测试交替设置值")
    void testAlternatingSetValues() {
        Classinfo classinfo = new Classinfo();
        
        classinfo.setName("班级A");
        classinfo.setNum(30);
        classinfo.setTeaid(1);
        
        assertEquals("班级A", classinfo.getName());
        assertEquals(30, classinfo.getNum());
        assertEquals(1, classinfo.getTeaid());
        
        classinfo.setName("班级B");
        classinfo.setNum(40);
        classinfo.setTeaid(2);
        
        assertEquals("班级B", classinfo.getName());
        assertEquals(40, classinfo.getNum());
        assertEquals(2, classinfo.getTeaid());
    }
}
