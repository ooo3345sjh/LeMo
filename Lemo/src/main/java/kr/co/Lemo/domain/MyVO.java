package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @since 2023/03/24
 * @author 박종협
 * @apiNote my에는 7가지 카테고리가 있음 그걸 통합하고자 생성함
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyVO {

    private List<PointVO> pointVO;
    private List<CouponVO> couponVO;
    private List<ReservationVO> reservationVO;
    private List<PickVO> pickVO;
    private List<ReviewVO> reviewVO;



}