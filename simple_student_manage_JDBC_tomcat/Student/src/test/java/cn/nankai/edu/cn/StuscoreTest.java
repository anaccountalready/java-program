package cn.nankai.edu.cn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stuscore实体类测试")
class StuscoreTest {

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        Stuscore score = new Stuscore();
        
        assertNotNull(score);
        assertEquals(0, score.getStuid());
        assertEquals(0, score.getCouid());
        assertEquals(0.0f, score.getScore(), 0.001f);
        assertNull(score.getCouname());
    }

    @ParameterizedTest
    @CsvSource({
            "1001, 1, 85.5",
            "1002, 2, 90.0",
            "1003, 3, 78.5"
    })
    @DisplayName("测试三参数构造函数")
    void testThreeParameterConstructor(int stuid, int couid, float score) {
        Stuscore stuscore = new Stuscore(stuid, couid, score);
        
        assertEquals(stuid, stuscore.getStuid());
        assertEquals(couid, stuscore.getCouid());
        assertEquals(score, stuscore.getScore(), 0.001f);
        assertNull(stuscore.getCouname());
    }

    @ParameterizedTest
    @CsvSource({
            "1001, 1, 85.5, 高等数学",
            "1002, 2, 90.0, 大学英语",
            "1003, 3, 78.5, 程序设计"
    })
    @DisplayName("测试四参数构造函数")
    void testFourParameterConstructor(int stuid, int couid, float score, String couname) {
        Stuscore stuscore = new Stuscore(stuid, couid, score, couname);
        
        assertEquals(stuid, stuscore.getStuid());
        assertEquals(couid, stuscore.getCouid());
        assertEquals(score, stuscore.getScore(), 0.001f);
        assertEquals(couname, stuscore.getCouname());
    }

    @Test
    @DisplayName("测试Setter方法")
    void testSetters() {
        Stuscore score = new Stuscore();
        
        score.setStuid(2001);
        score.setCouid(10);
        score.setScore(95.5f);
        score.setcouname("数据结构");
        
        assertEquals(2001, score.getStuid());
        assertEquals(10, score.getCouid());
        assertEquals(95.5f, score.getScore(), 0.001f);
        assertEquals("数据结构", score.getCouname());
    }

    @Test
    @DisplayName("测试边界值 - 分数为0")
    void testBoundaryScoreZero() {
        Stuscore score = new Stuscore(1001, 1, 0.0f);
        assertEquals(0.0f, score.getScore(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - 分数为100")
    void testBoundaryScoreHundred() {
        Stuscore score = new Stuscore(1001, 1, 100.0f);
        assertEquals(100.0f, score.getScore(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - 分数为负数")
    void testBoundaryNegativeScore() {
        Stuscore score = new Stuscore(1001, 1, -5.0f);
        assertEquals(-5.0f, score.getScore(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - 分数超过100")
    void testBoundaryScoreOverHundred() {
        Stuscore score = new Stuscore(1001, 1, 105.5f);
        assertEquals(105.5f, score.getScore(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - ID为0")
    void testBoundaryZeroId() {
        Stuscore score = new Stuscore(0, 0, 80.0f);
        assertEquals(0, score.getStuid());
        assertEquals(0, score.getCouid());
    }

    @Test
    @DisplayName("测试边界值 - ID为负数")
    void testBoundaryNegativeId() {
        Stuscore score = new Stuscore(-1, -2, 80.0f);
        assertEquals(-1, score.getStuid());
        assertEquals(-2, score.getCouid());
    }

    @Test
    @DisplayName("测试边界值 - ID为最大值")
    void testBoundaryMaxId() {
        Stuscore score = new Stuscore(Integer.MAX_VALUE, Integer.MAX_VALUE, 80.0f);
        assertEquals(Integer.MAX_VALUE, score.getStuid());
        assertEquals(Integer.MAX_VALUE, score.getCouid());
    }

    @Test
    @DisplayName("测试空课程名")
    void testNullCouname() {
        Stuscore score = new Stuscore(1001, 1, 85.5f, null);
        assertNull(score.getCouname());
        
        score.setcouname(null);
        assertNull(score.getCouname());
    }

    @Test
    @DisplayName("测试空字符串课程名")
    void testEmptyCouname() {
        Stuscore score = new Stuscore(1001, 1, 85.5f, "");
        assertEquals("", score.getCouname());
        
        score.setcouname("");
        assertEquals("", score.getCouname());
    }

    @Test
    @DisplayName("测试长课程名")
    void testLongCouname() {
        String longName = "这是一个非常长的课程名称".repeat(10);
        Stuscore score = new Stuscore(1001, 1, 85.5f, longName);
        assertEquals(longName, score.getCouname());
        
        score.setcouname(longName);
        assertEquals(longName, score.getCouname());
    }

    @Test
    @DisplayName("测试分数精度")
    void testScorePrecision() {
        Stuscore score = new Stuscore();
        
        score.setScore(85.123456f);
        assertEquals(85.123456f, score.getScore(), 0.000001f);
        
        score.setScore(0.0001f);
        assertEquals(0.0001f, score.getScore(), 0.0000001f);
    }

    @Test
    @DisplayName("测试分数NaN和无穷大")
    void testSpecialScoreValues() {
        Stuscore score = new Stuscore();
        
        score.setScore(Float.NaN);
        assertTrue(Float.isNaN(score.getScore()));
        
        score.setScore(Float.POSITIVE_INFINITY);
        assertTrue(Float.isInfinite(score.getScore()));
        
        score.setScore(Float.NEGATIVE_INFINITY);
        assertTrue(Float.isInfinite(score.getScore()));
    }
}
