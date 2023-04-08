package kr.co.Lemo;

import kr.co.Lemo.dao.ProductDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class productReservationTest {

    @Autowired
    private ProductDAO dao;

    @Test
    @DisplayName("1. 예약 번호 수정 테스트")
    public void usaveResNo() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        String format = formatter.format(new Date());
        Long res_no = Long.parseLong(format + "000000");
        Assertions.assertEquals(dao.usaveResNo(res_no), 0);
    }
}
