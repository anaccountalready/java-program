package cn.nankai.edu.cn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student实体类测试")
class StudentTest {

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        Student student = new Student();
        
        assertNotNull(student);
        assertEquals(1, student.getId());
        assertEquals("name", student.getName());
        assertEquals("sex", student.getSex());
        assertNotNull(student.getIndate());
        assertEquals("claname", student.getClaname());
        assertEquals("major", student.getMajor());
        assertEquals("tea", student.getTeacher());
    }

    @ParameterizedTest
    @CsvSource({
            "1001, 张三, 男, 2023-09-01, 计算机一班, 1, 1",
            "1002, 李四, 女, 2023-09-02, 计算机二班, 2, 2",
            "1003, 王五, 男, 2023-09-03, 计算机三班, 3, 3"
    })
    @DisplayName("测试带参数构造函数和Getter方法")
    void testParameterizedConstructorAndGetters(int id, String name, String sex, 
            String dateStr, String claname, int majorId, int teaId) {
        Date indate = Date.valueOf(dateStr);
        Student student = new Student(id, name, sex, indate, claname, majorId, teaId);
        
        assertEquals(id, student.getId());
        assertEquals(name, student.getName());
        assertEquals(sex, student.getSex());
        assertEquals(indate, student.getIndate());
        assertEquals(claname, student.getClaname());
        assertEquals(String.valueOf(majorId), student.getMajor());
        assertEquals(String.valueOf(teaId), student.getTeacher());
    }

    @Test
    @DisplayName("测试Setter方法")
    void testSetters() {
        Student student = new Student();
        
        student.setId(2001);
        student.setName("测试学生");
        student.setSex("女");
        Date newDate = Date.valueOf("2024-01-01");
        student.setIndate(newDate);
        student.setClaname("测试班级");
        student.setMajor("测试专业");
        student.setTeacher("测试教师");
        
        assertEquals(2001, student.getId());
        assertEquals("测试学生", student.getName());
        assertEquals("女", student.getSex());
        assertEquals(newDate, student.getIndate());
        assertEquals("测试班级", student.getClaname());
        assertEquals("测试专业", student.getMajor());
        assertEquals("测试教师", student.getTeacher());
    }

    @Test
    @DisplayName("测试空值处理")
    void testNullValues() {
        Student student = new Student();
        
        student.setName(null);
        student.setSex(null);
        student.setClaname(null);
        student.setMajor(null);
        student.setTeacher(null);
        
        assertNull(student.getName());
        assertNull(student.getSex());
        assertNull(student.getClaname());
        assertNull(student.getMajor());
        assertNull(student.getTeacher());
    }

    @Test
    @DisplayName("测试边界值 - ID为0")
    void testBoundaryValueZeroId() {
        Student student = new Student(0, "零号", "男", Date.valueOf("2023-01-01"), "班级", 1, 1);
        assertEquals(0, student.getId());
    }

    @Test
    @DisplayName("测试边界值 - ID为负数")
    void testBoundaryValueNegativeId() {
        Student student = new Student(-1, "负号", "男", Date.valueOf("2023-01-01"), "班级", 1, 1);
        assertEquals(-1, student.getId());
    }

    @Test
    @DisplayName("测试边界值 - ID为最大值")
    void testBoundaryValueMaxId() {
        Student student = new Student(Integer.MAX_VALUE, "最大号", "男", Date.valueOf("2023-01-01"), "班级", 1, 1);
        assertEquals(Integer.MAX_VALUE, student.getId());
    }

    @Test
    @DisplayName("测试日期边界值")
    void testBoundaryDateValues() {
        Student student = new Student();
        
        Date minDate = Date.valueOf("0001-01-01");
        student.setIndate(minDate);
        assertEquals(minDate, student.getIndate());
        
        Date maxDate = Date.valueOf("9999-12-31");
        student.setIndate(maxDate);
        assertEquals(maxDate, student.getIndate());
    }

    @Test
    @DisplayName("测试长字符串处理")
    void testLongStrings() {
        Student student = new Student();
        
        String longName = "a".repeat(1000);
        student.setName(longName);
        assertEquals(longName, student.getName());
        
        String longClaname = "b".repeat(1000);
        student.setClaname(longClaname);
        assertEquals(longClaname, student.getClaname());
    }
}
